package mk.ukim.finki.soa.graduationthesis.application.saga

import mk.ukim.finki.soa.graduationthesis.domain.event.*
import mk.ukim.finki.soa.graduationthesis.infrastructure.client.EmailNotificationRequest
import mk.ukim.finki.soa.graduationthesis.infrastructure.client.NotificationServiceClient
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.lang.invoke.MethodHandles

@Saga
class ThesisLifecycleSaga {
    private val logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

    @Autowired
    @Transient
    private lateinit var notificationServiceClient: NotificationServiceClient

    @StartSaga
    @SagaEventHandler(associationProperty = "thesisId")
    fun on(event: ThesisCreatedEvent) {
        logger.info("Starting saga for thesis ${event.thesisId.value}")
        SagaLifecycle.associateWith("studentId", event.studentId)
        SagaLifecycle.associateWith("mentorId", event.mentorId)

        // Send notification to student
        try {
            notificationServiceClient.sendEmail(
                EmailNotificationRequest(
                    to = event.studentId,
                    subject = "New Thesis Proposal",
                    body = "You have been assigned to a new thesis: ${event.title}. Please review and accept it."
                )
            )
            logger.info("Notification sent to student ${event.studentId}")
        } catch (e: Exception) {
            logger.error("Failed to send notification to student: ${e.message}")
        }
    }

    @SagaEventHandler(associationProperty = "thesisId")
    fun on(event: ThesisStatusChangedEvent) {
        logger.info("Thesis ${event.thesisId.value} status changed from ${event.previousStatus} to ${event.newStatus}")

        // Here you could trigger notifications based on status changes
        try {
            when (event.newStatus) {
                "STUDENT_APPROVED" -> {
                    notificationServiceClient.sendEmail(
                        EmailNotificationRequest(
                            to = "administration@university.edu",
                            subject = "Thesis Proposal Accepted by Student",
                            body = "A thesis proposal (ID: ${event.thesisId.value}) has been accepted by the student and is ready for administration validation."
                        )
                    )
                    logger.info("Notification sent to administration")
                }
                "ADMINISTRATION_VALIDATED" -> {
                    notificationServiceClient.sendEmail(
                        EmailNotificationRequest(
                            to = "vicedean@university.edu",
                            subject = "Thesis Proposal Validated by Administration",
                            body = "A thesis proposal (ID: ${event.thesisId.value}) has been validated by administration and is ready for your approval."
                        )
                    )
                    logger.info("Notification sent to vice dean")
                }
                "DEFENSE_SCHEDULED" -> {
                    logger.info("Defense scheduled for thesis ${event.thesisId.value}")
                    // Notifications are sent in the service layer
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to send status change notification: ${e.message}")
        }
    }

    @SagaEventHandler(associationProperty = "thesisId")
    fun on(event: ThesisProposalAcceptedEvent) {
        logger.info("Thesis proposal ${event.thesisId.value} accepted by student ${event.studentId}")

        try {
            // Notify mentor that student has accepted the proposal
            val thesis = getThesisDetails(event.thesisId.value)
            if (thesis != null) {
                notificationServiceClient.sendEmail(
                    EmailNotificationRequest(
                        to = thesis.mentorId,
                        subject = "Student Accepted Thesis Proposal",
                        body = "The student has accepted your thesis proposal '${thesis.title}'. The proposal will now be reviewed by administration."
                    )
                )
            }
        } catch (e: Exception) {
            logger.error("Failed to send proposal acceptance notification: ${e.message}")
        }
    }

    @SagaEventHandler(associationProperty = "thesisId")
    fun on(event: ThesisDefenseCompletedEvent) {
        logger.info("Thesis ${event.thesisId.value} defense completed with grade ${event.grade}")

        // Here you could trigger graduation processes or notifications
        try {
            notificationServiceClient.sendEmail(
                EmailNotificationRequest(
                    to = "studentaffairs@university.edu",
                    subject = "Thesis Defense Completed",
                    body = "A thesis defense (ID: ${event.thesisId.value}) has been completed with grade ${event.grade}. Please process the graduation."
                )
            )

            // Also notify the student about their grade
            val thesis = getThesisDetails(event.thesisId.value)
            if (thesis != null) {
                notificationServiceClient.sendEmail(
                    EmailNotificationRequest(
                        to = thesis.studentId,
                        subject = "Thesis Defense Result",
                        body = "Congratulations! Your thesis defense has been completed with a grade of ${event.grade}."
                    )
                )
            }

            logger.info("Defense completion notifications sent")
        } catch (e: Exception) {
            logger.error("Failed to send defense completion notification: ${e.message}")
        }
    }

    @SagaEventHandler(associationProperty = "thesisId")
    fun on(event: ThesisCanceledEvent) {
        logger.info("Thesis ${event.thesisId.value} canceled: ${event.reason}")

        try {
            // Notify relevant parties about cancellation
            val thesis = getThesisDetails(event.thesisId.value)
            if (thesis != null) {
                val participants = listOf(thesis.studentId, thesis.mentorId, thesis.firstMemberId, thesis.secondMemberId)
                participants.forEach { participantId ->
                    notificationServiceClient.sendEmail(
                        EmailNotificationRequest(
                            to = participantId,
                            subject = "Thesis Canceled",
                            body = "The thesis '${thesis.title}' has been canceled. Reason: ${event.reason}"
                        )
                    )
                }
            }
            logger.info("Cancellation notifications sent")
        } catch (e: Exception) {
            logger.error("Failed to send cancellation notifications: ${e.message}")
        }

        // End the saga
        SagaLifecycle.end()
    }

    // Helper method to get thesis details - in a real implementation, this would query the database
    private fun getThesisDetails(thesisId: String): ThesisDetails? {
        // In a real implementation, you would inject a repository or service to fetch thesis details
        // For now, we'll return null to avoid compilation errors
        logger.info(thesisId)
        return null
    }

    // Simple data class to hold thesis details
    private data class ThesisDetails(
        val thesisId: String,
        val title: String,
        val studentId: String,
        val mentorId: String,
        val firstMemberId: String,
        val secondMemberId: String
    )
}