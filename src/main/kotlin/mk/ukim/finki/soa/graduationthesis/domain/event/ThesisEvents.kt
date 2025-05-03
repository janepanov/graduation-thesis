package mk.ukim.finki.soa.graduationthesis.domain.event

import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisId
import java.time.Instant

// Thesis created event
data class ThesisCreatedEvent(
    val thesisId: ThesisId,
    val title: String,
    val area: String,
    val description: String,
    val studentId: String,
    val mentorId: String,
    val firstMemberId: String,
    val secondMemberId: String,
    val createdAt: Instant
)

// Thesis proposal accepted by student
data class ThesisProposalAcceptedEvent(
    val thesisId: ThesisId,
    val studentId: String,
    val acceptedAt: Instant
)

// Thesis proposal validated by administration
data class ThesisProposalValidatedEvent(
    val thesisId: ThesisId,
    val adminId: String,
    val isValid: Boolean,
    val reason: String?,
    val validatedAt: Instant
)

// Thesis proposal approved by vice dean
data class ThesisProposalApprovedEvent(
    val thesisId: ThesisId,
    val viceDeanId: String,
    val isApproved: Boolean,
    val reason: String?,
    val approvedAt: Instant
)

// Thesis text uploaded
data class ThesisTextUploadedEvent(
    val thesisId: ThesisId,
    val uploaderId: String,
    val fileId: String,
    val fileName: String,
    val uploadedAt: Instant
)

// Thesis text approved by mentor
data class ThesisTextMentorApprovedEvent(
    val thesisId: ThesisId,
    val mentorId: String,
    val isApproved: Boolean,
    val comments: String?,
    val approvedAt: Instant
)

// Thesis text approved by committee member
data class ThesisTextCommitteeMemberApprovedEvent(
    val thesisId: ThesisId,
    val memberId: String,
    val memberRole: String,
    val isApproved: Boolean,
    val comments: String?,
    val approvedAt: Instant
)

// Thesis defense scheduled
data class ThesisDefenseScheduledEvent(
    val thesisId: ThesisId,
    val schedulerId: String,
    val location: String,
    val presentationDateTime: String,
    val scheduledAt: Instant
)

// Thesis defense completed
data class ThesisDefenseCompletedEvent(
    val thesisId: ThesisId,
    val mentorId: String,
    val grade: Int,
    val comments: String?,
    val completedAt: Instant
)

// Thesis canceled
data class ThesisCanceledEvent(
    val thesisId: ThesisId,
    val cancelerId: String,
    val reason: String,
    val canceledAt: Instant
)

// Thesis status changed
data class ThesisStatusChangedEvent(
    val thesisId: ThesisId,
    val previousStatus: String,
    val newStatus: String,
    val changedAt: Instant
)