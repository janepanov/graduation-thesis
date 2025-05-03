package mk.ukim.finki.soa.graduationthesis.domain.command

import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisId
import org.axonframework.modelling.command.TargetAggregateIdentifier

// Create a new thesis
data class CreateThesisCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val title: String,
    val area: String,
    val description: String,
    val studentId: String,
    val mentorId: String,
    val firstMemberId: String,
    val secondMemberId: String
)

// Student accepts the thesis proposal
data class AcceptThesisProposalCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val studentId: String
)

// Administration validates the thesis proposal
data class ValidateThesisProposalCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val adminId: String,
    val isValid: Boolean,
    val reason: String? = null
)

// Vice dean approves the thesis proposal
data class ApproveThesisProposalCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val viceDeanId: String,
    val isApproved: Boolean,
    val reason: String? = null
)

// Upload thesis text
data class UploadThesisTextCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val uploaderId: String,
    val fileId: String,
    val fileName: String
)

// Mentor approves thesis text
data class ApproveMentorThesisTextCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val mentorId: String,
    val isApproved: Boolean,
    val comments: String? = null
)

// Committee member approves thesis text
data class ApproveCommitteeMemberThesisTextCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val memberId: String,
    val memberRole: String, // "FIRST_MEMBER" or "SECOND_MEMBER"
    val isApproved: Boolean,
    val comments: String? = null
)

// Schedule thesis defense
data class ScheduleThesisDefenseCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val schedulerId: String,
    val location: String,
    val presentationDateTime: String
)

// Complete thesis defense
data class CompleteThesisDefenseCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val mentorId: String,
    val grade: Int,
    val comments: String? = null
)

// Cancel thesis
data class CancelThesisCommand(
    @TargetAggregateIdentifier val thesisId: ThesisId,
    val cancelerId: String,
    val reason: String
)