package mk.ukim.finki.soa.graduationthesis.domain.aggregate

import mk.ukim.finki.soa.graduationthesis.domain.command.*
import mk.ukim.finki.soa.graduationthesis.domain.event.*
import mk.ukim.finki.soa.graduationthesis.domain.exception.InvalidGradeException
import mk.ukim.finki.soa.graduationthesis.domain.exception.InvalidThesisDataException
import mk.ukim.finki.soa.graduationthesis.domain.exception.InvalidThesisStateException
import mk.ukim.finki.soa.graduationthesis.domain.exception.UnauthorizedThesisOperationException
import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisId
import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisStatus
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.time.Instant

@Aggregate
class ThesisAggregate {

    @AggregateIdentifier
    private lateinit var thesisId: ThesisId
    private lateinit var title: String
    private lateinit var area: String
    private lateinit var description: String
    private lateinit var studentId: String
    private lateinit var mentorId: String
    private lateinit var firstMemberId: String
    private lateinit var secondMemberId: String
    private lateinit var status: ThesisStatus
    private var fileId: String? = null
    private var fileName: String? = null
    private var location: String? = null
    private var presentationDateTime: String? = null
    private var grade: Int? = null
    private val committeeApprovals = mutableMapOf<String, Boolean>()

    constructor() // Required by Axon

    @CommandHandler
    constructor(command: CreateThesisCommand) {
        validateCreateThesisCommand(command)

        AggregateLifecycle.apply(
            ThesisCreatedEvent(
                thesisId = command.thesisId,
                title = command.title,
                area = command.area,
                description = command.description,
                studentId = command.studentId,
                mentorId = command.mentorId,
                firstMemberId = command.firstMemberId,
                secondMemberId = command.secondMemberId,
                createdAt = Instant.now()
            )
        )

        AggregateLifecycle.apply(
            ThesisStatusChangedEvent(
                thesisId = command.thesisId,
                previousStatus = "",
                newStatus = ThesisStatus.DRAFT.name,
                changedAt = Instant.now()
            )
        )
    }

    private fun validateCreateThesisCommand(command: CreateThesisCommand) {
        if (command.title.isBlank()) throw InvalidThesisDataException("Title cannot be empty")
        if (command.area.isBlank()) throw InvalidThesisDataException("Area cannot be empty")
        if (command.description.isBlank()) throw InvalidThesisDataException("Description cannot be empty")
        if (command.studentId.isBlank()) throw InvalidThesisDataException("Student ID cannot be empty")
        if (command.mentorId.isBlank()) throw InvalidThesisDataException("Mentor ID cannot be empty")
        if (command.firstMemberId.isBlank()) throw InvalidThesisDataException("First member ID cannot be empty")
        if (command.secondMemberId.isBlank()) throw InvalidThesisDataException("Second member ID cannot be empty")
        if (command.firstMemberId == command.secondMemberId)
            throw InvalidThesisDataException("First and second members cannot be the same")
    }

    @CommandHandler
    fun handle(command: AcceptThesisProposalCommand) {
        if (status != ThesisStatus.DRAFT)
            throw InvalidThesisStateException("Thesis proposal can only be accepted when in DRAFT status")
        if (command.studentId != studentId)
            throw UnauthorizedThesisOperationException("Only the assigned student can accept the thesis proposal")

        AggregateLifecycle.apply(
            ThesisProposalAcceptedEvent(
                thesisId = command.thesisId,
                studentId = command.studentId,
                acceptedAt = Instant.now()
            )
        )

        AggregateLifecycle.apply(
            ThesisStatusChangedEvent(
                thesisId = command.thesisId,
                previousStatus = status.name,
                newStatus = ThesisStatus.STUDENT_APPROVED.name,
                changedAt = Instant.now()
            )
        )
    }

    @CommandHandler
    fun handle(command: ValidateThesisProposalCommand) {
        if (status != ThesisStatus.STUDENT_APPROVED)
            throw InvalidThesisStateException("Thesis proposal can only be validated when in STUDENT_APPROVED status")

        AggregateLifecycle.apply(
            ThesisProposalValidatedEvent(
                thesisId = command.thesisId,
                adminId = command.adminId,
                isValid = command.isValid,
                reason = command.reason,
                validatedAt = Instant.now()
            )
        )

        if (command.isValid) {
            AggregateLifecycle.apply(
                ThesisStatusChangedEvent(
                    thesisId = command.thesisId,
                    previousStatus = status.name,
                    newStatus = ThesisStatus.ADMINISTRATION_VALIDATED.name,
                    changedAt = Instant.now()
                )
            )
        } else {
            AggregateLifecycle.apply(
                ThesisStatusChangedEvent(
                    thesisId = command.thesisId,
                    previousStatus = status.name,
                    newStatus = ThesisStatus.REJECTED.name,
                    changedAt = Instant.now()
                )
            )
        }
    }

    @CommandHandler
    fun handle(command: ApproveThesisProposalCommand) {
        if (status != ThesisStatus.ADMINISTRATION_VALIDATED)
            throw InvalidThesisStateException("Thesis proposal can only be approved when in ADMINISTRATION_VALIDATED status")

        AggregateLifecycle.apply(
            ThesisProposalApprovedEvent(
                thesisId = command.thesisId,
                viceDeanId = command.viceDeanId,
                isApproved = command.isApproved,
                reason = command.reason,
                approvedAt = Instant.now()
            )
        )

        if (command.isApproved) {
            AggregateLifecycle.apply(
                ThesisStatusChangedEvent(
                    thesisId = command.thesisId,
                    previousStatus = status.name,
                    newStatus = ThesisStatus.VICE_DEAN_APPROVED.name,
                    changedAt = Instant.now()
                )
            )
        } else {
            AggregateLifecycle.apply(
                ThesisStatusChangedEvent(
                    thesisId = command.thesisId,
                    previousStatus = status.name,
                    newStatus = ThesisStatus.REJECTED.name,
                    changedAt = Instant.now()
                )
            )
        }
    }

    @CommandHandler
    fun handle(command: UploadThesisTextCommand) {
        if (status != ThesisStatus.VICE_DEAN_APPROVED)
            throw InvalidThesisStateException("Thesis text can only be uploaded when in VICE_DEAN_APPROVED status")

        AggregateLifecycle.apply(
            ThesisTextUploadedEvent(
                thesisId = command.thesisId,
                uploaderId = command.uploaderId,
                fileId = command.fileId,
                fileName = command.fileName,
                uploadedAt = Instant.now()
            )
        )

        AggregateLifecycle.apply(
            ThesisStatusChangedEvent(
                thesisId = command.thesisId,
                previousStatus = status.name,
                newStatus = ThesisStatus.MENTOR_APPROVAL_PENDING.name,
                changedAt = Instant.now()
            )
        )
    }

    @CommandHandler
    fun handle(command: ApproveMentorThesisTextCommand) {
        if (status != ThesisStatus.MENTOR_APPROVAL_PENDING)
            throw InvalidThesisStateException("Thesis text can only be approved by mentor when in MENTOR_APPROVAL_PENDING status")
        if (command.mentorId != mentorId)
            throw UnauthorizedThesisOperationException("Only the assigned mentor can approve the thesis text")

        AggregateLifecycle.apply(
            ThesisTextMentorApprovedEvent(
                thesisId = command.thesisId,
                mentorId = command.mentorId,
                isApproved = command.isApproved,
                comments = command.comments,
                approvedAt = Instant.now()
            )
        )

        if (command.isApproved) {
            AggregateLifecycle.apply(
                ThesisStatusChangedEvent(
                    thesisId = command.thesisId,
                    previousStatus = status.name,
                    newStatus = ThesisStatus.COMMITTEE_APPROVAL_PENDING.name,
                    changedAt = Instant.now()
                )
            )
        } else {
            AggregateLifecycle.apply(
                ThesisStatusChangedEvent(
                    thesisId = command.thesisId,
                    previousStatus = status.name,
                    newStatus = ThesisStatus.THESIS_TEXT_PENDING.name,
                    changedAt = Instant.now()
                )
            )
        }
    }

    @CommandHandler
    fun handle(command: ApproveCommitteeMemberThesisTextCommand) {
        if (status != ThesisStatus.COMMITTEE_APPROVAL_PENDING)
            throw InvalidThesisStateException("Thesis text can only be approved by committee when in COMMITTEE_APPROVAL_PENDING status")

        val isValidMember = when (command.memberRole) {
            "FIRST_MEMBER" -> command.memberId == firstMemberId
            "SECOND_MEMBER" -> command.memberId == secondMemberId
            else -> false
        }

        if (!isValidMember)
            throw UnauthorizedThesisOperationException("Only assigned committee members can approve the thesis text")

        AggregateLifecycle.apply(
            ThesisTextCommitteeMemberApprovedEvent(
                thesisId = command.thesisId,
                memberId = command.memberId,
                memberRole = command.memberRole,
                isApproved = command.isApproved,
                comments = command.comments,
                approvedAt = Instant.now()
            )
        )

        // Update committee approvals
        committeeApprovals[command.memberRole] = command.isApproved

        // Check if all committee members have approved
        if (committeeApprovals.size == 2 && committeeApprovals.values.all { it }) {
            AggregateLifecycle.apply(
                ThesisStatusChangedEvent(
                    thesisId = command.thesisId,
                    previousStatus = status.name,
                    newStatus = ThesisStatus.DEFENSE_SCHEDULING_PENDING.name,
                    changedAt = Instant.now()
                )
            )
        }
    }

    @CommandHandler
    fun handle(command: ScheduleThesisDefenseCommand) {
        if (status != ThesisStatus.DEFENSE_SCHEDULING_PENDING)
            throw InvalidThesisStateException("Thesis defense can only be scheduled when in DEFENSE_SCHEDULING_PENDING status")

        AggregateLifecycle.apply(
            ThesisDefenseScheduledEvent(
                thesisId = command.thesisId,
                schedulerId = command.schedulerId,
                location = command.location,
                presentationDateTime = command.presentationDateTime,
                scheduledAt = Instant.now()
            )
        )

        AggregateLifecycle.apply(
            ThesisStatusChangedEvent(
                thesisId = command.thesisId,
                previousStatus = status.name,
                newStatus = ThesisStatus.DEFENSE_SCHEDULED.name,
                changedAt = Instant.now()
            )
        )
    }

    @CommandHandler
    fun handle(command: CompleteThesisDefenseCommand) {
        if (status != ThesisStatus.DEFENSE_SCHEDULED)
            throw InvalidThesisStateException("Thesis defense can only be completed when in DEFENSE_SCHEDULED status")
        if (command.mentorId != mentorId)
            throw UnauthorizedThesisOperationException("Only the assigned mentor can complete the thesis defense")
        if (command.grade !in 5..10)
            throw InvalidGradeException(command.grade)

        AggregateLifecycle.apply(
            ThesisDefenseCompletedEvent(
                thesisId = command.thesisId,
                mentorId = command.mentorId,
                grade = command.grade,
                comments = command.comments,
                completedAt = Instant.now()
            )
        )

        AggregateLifecycle.apply(
            ThesisStatusChangedEvent(
                thesisId = command.thesisId,
                previousStatus = status.name,
                newStatus = ThesisStatus.GRADED.name,
                changedAt = Instant.now()
            )
        )
    }

    @CommandHandler
    fun handle(command: CancelThesisCommand) {
        if (status == ThesisStatus.CANCELED || status == ThesisStatus.ARCHIVED)
            throw InvalidThesisStateException("Thesis that is already canceled or archived cannot be canceled")

        AggregateLifecycle.apply(
            ThesisCanceledEvent(
                thesisId = command.thesisId,
                cancelerId = command.cancelerId,
                reason = command.reason,
                canceledAt = Instant.now()
            )
        )

        AggregateLifecycle.apply(
            ThesisStatusChangedEvent(
                thesisId = command.thesisId,
                previousStatus = status.name,
                newStatus = ThesisStatus.CANCELED.name,
                changedAt = Instant.now()
            )
        )
    }

    @EventSourcingHandler
    fun on(event: ThesisCreatedEvent) {
        thesisId = event.thesisId
        title = event.title
        area = event.area
        description = event.description
        studentId = event.studentId
        mentorId = event.mentorId
        firstMemberId = event.firstMemberId
        secondMemberId = event.secondMemberId
    }

    @EventSourcingHandler
    fun on(event: ThesisStatusChangedEvent) {
        status = ThesisStatus.valueOf(event.newStatus)
    }

    @EventSourcingHandler
    fun on(event: ThesisTextUploadedEvent) {
        fileId = event.fileId
        fileName = event.fileName
    }

    @EventSourcingHandler
    fun on(event: ThesisTextCommitteeMemberApprovedEvent) {
        committeeApprovals[event.memberRole] = event.isApproved
    }

    @EventSourcingHandler
    fun on(event: ThesisDefenseScheduledEvent) {
        location = event.location
        presentationDateTime = event.presentationDateTime
    }

    @EventSourcingHandler
    fun on(event: ThesisDefenseCompletedEvent) {
        grade = event.grade
    }
}