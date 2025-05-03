package mk.ukim.finki.soa.graduationthesis.application.service

import mk.ukim.finki.soa.graduationthesis.domain.command.*
import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisId
import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisStatus
import mk.ukim.finki.soa.graduationthesis.infrastructure.client.EmailNotificationRequest
import mk.ukim.finki.soa.graduationthesis.infrastructure.client.NotificationServiceClient
import mk.ukim.finki.soa.graduationthesis.infrastructure.persistence.ThesisView
import mk.ukim.finki.soa.graduationthesis.infrastructure.persistence.ThesisViewRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class ThesisServiceImpl(
    private val commandGateway: CommandGateway,
    private val thesisViewRepository: ThesisViewRepository,
    private val notificationServiceClient: NotificationServiceClient
) : ThesisService {

    override fun createThesis(
        title: String,
        area: String,
        description: String,
        studentId: String,
        mentorId: String,
        firstMemberId: String,
        secondMemberId: String
    ): CompletableFuture<String> {
        val thesisId = ThesisId(UUID.randomUUID().toString())

        val command = CreateThesisCommand(
            thesisId = thesisId,
            title = title,
            area = area,
            description = description,
            studentId = studentId,
            mentorId = mentorId,
            firstMemberId = firstMemberId,
            secondMemberId = secondMemberId
        )

        val result = commandGateway.send<String>(command)

        // Send notification to student
        notificationServiceClient.sendEmail(
            EmailNotificationRequest(
                to = studentId,
                subject = "New Thesis Proposal",
                body = "You have been assigned to a new thesis: $title. Please review and accept it."
            )
        )

        return result
    }

    override fun acceptThesisProposal(thesisId: String, studentId: String): CompletableFuture<String> {
        val command = AcceptThesisProposalCommand(
            thesisId = ThesisId(thesisId),
            studentId = studentId
        )

        return commandGateway.send(command)
    }

    override fun validateThesisProposal(
        thesisId: String,
        adminId: String,
        isValid: Boolean,
        reason: String?
    ): CompletableFuture<String> {
        val command = ValidateThesisProposalCommand(
            thesisId = ThesisId(thesisId),
            adminId = adminId,
            isValid = isValid,
            reason = reason
        )

        return commandGateway.send(command)
    }

    override fun approveThesisProposal(
        thesisId: String,
        viceDeanId: String,
        isApproved: Boolean,
        reason: String?
    ): CompletableFuture<String> {
        val command = ApproveThesisProposalCommand(
            thesisId = ThesisId(thesisId),
            viceDeanId = viceDeanId,
            isApproved = isApproved,
            reason = reason
        )

        return commandGateway.send(command)
    }

    override fun uploadThesisText(
        thesisId: String,
        uploaderId: String,
        fileId: String,
        fileName: String
    ): CompletableFuture<String> {
        val command = UploadThesisTextCommand(
            thesisId = ThesisId(thesisId),
            uploaderId = uploaderId,
            fileId = fileId,
            fileName = fileName
        )

        val result = commandGateway.send<String>(command)

        // Notify mentor
        val thesis = getThesis(thesisId)
        if (thesis != null) {
            notificationServiceClient.sendEmail(
                EmailNotificationRequest(
                    to = thesis.mentorId,
                    subject = "Thesis Text Uploaded",
                    body = "The thesis text for '${thesis.title}' has been uploaded. Please review it."
                )
            )
        }

        return result
    }

    override fun approveMentorThesisText(
        thesisId: String,
        mentorId: String,
        isApproved: Boolean,
        comments: String?
    ): CompletableFuture<String> {
        val command = ApproveMentorThesisTextCommand(
            thesisId = ThesisId(thesisId),
            mentorId = mentorId,
            isApproved = isApproved,
            comments = comments
        )

        return commandGateway.send(command)
    }

    override fun approveCommitteeMemberThesisText(
        thesisId: String,
        memberId: String,
        memberRole: String,
        isApproved: Boolean,
        comments: String?
    ): CompletableFuture<String> {
        val command = ApproveCommitteeMemberThesisTextCommand(
            thesisId = ThesisId(thesisId),
            memberId = memberId,
            memberRole = memberRole,
            isApproved = isApproved,
            comments = comments
        )

        return commandGateway.send(command)
    }

    override fun scheduleThesisDefense(
        thesisId: String,
        schedulerId: String,
        location: String,
        presentationDateTime: String
    ): CompletableFuture<String> {
        val command = ScheduleThesisDefenseCommand(
            thesisId = ThesisId(thesisId),
            schedulerId = schedulerId,
            location = location,
            presentationDateTime = presentationDateTime
        )

        val result = commandGateway.send<String>(command)

        // Notify all participants
        val thesis = getThesis(thesisId)
        if (thesis != null) {
            val participants = listOf(thesis.studentId, thesis.mentorId, thesis.firstMemberId, thesis.secondMemberId)
            participants.forEach { participantId ->
                notificationServiceClient.sendEmail(
                    EmailNotificationRequest(
                        to = participantId,
                        subject = "Thesis Defense Scheduled",
                        body = "The defense for thesis '${thesis.title}' has been scheduled at $location on $presentationDateTime."
                    )
                )
            }
        }

        return result
    }

    override fun completeThesisDefense(
        thesisId: String,
        mentorId: String,
        grade: Int,
        comments: String?
    ): CompletableFuture<String> {
        val command = CompleteThesisDefenseCommand(
            thesisId = ThesisId(thesisId),
            mentorId = mentorId,
            grade = grade,
            comments = comments
        )

        return commandGateway.send(command)
    }

    override fun cancelThesis(
        thesisId: String,
        cancelerId: String,
        reason: String
    ): CompletableFuture<String> {
        val command = CancelThesisCommand(
            thesisId = ThesisId(thesisId),
            cancelerId = cancelerId,
            reason = reason
        )

        return commandGateway.send(command)
    }

    override fun getThesis(thesisId: String): ThesisView? {
        return thesisViewRepository.findById(thesisId).orElse(null)
    }

    override fun getThesesByStudent(studentId: String): List<ThesisView> {
        return thesisViewRepository.findByStudentId(studentId)
    }

    override fun getThesesByMentor(mentorId: String): List<ThesisView> {
        return thesisViewRepository.findByMentorId(mentorId)
    }

    override fun getThesesByStatus(status: ThesisStatus): List<ThesisView> {
        return thesisViewRepository.findByStatus(status)
    }

    override fun getAllTheses(): List<ThesisView> {
        return thesisViewRepository.findAll()
    }
}