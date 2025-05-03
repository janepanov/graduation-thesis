package mk.ukim.finki.soa.graduationthesis.presentation.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// Create thesis request
data class CreateThesisRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    val title: String,

    @field:NotBlank(message = "Area is required")
    val area: String,

    @field:NotBlank(message = "Description is required")
    @field:Size(min = 10, message = "Description must be at least 10 characters")
    val description: String,

    @field:NotBlank(message = "Student ID is required")
    val studentId: String,

    @field:NotBlank(message = "Mentor ID is required")
    val mentorId: String,

    @field:NotBlank(message = "First member ID is required")
    val firstMemberId: String,

    @field:NotBlank(message = "Second member ID is required")
    val secondMemberId: String
)

// Accept thesis proposal request
data class AcceptThesisProposalRequest(
    @field:NotBlank(message = "Student ID is required")
    val studentId: String
)

// Validate thesis proposal request
data class ValidateThesisProposalRequest(
    @field:NotBlank(message = "Admin ID is required")
    val adminId: String,

    val isValid: Boolean,

    val reason: String? = null
)

// Approve thesis proposal request
data class ApproveThesisProposalRequest(
    @field:NotBlank(message = "Vice Dean ID is required")
    val viceDeanId: String,

    val isApproved: Boolean,

    val reason: String? = null
)

// Upload thesis text request
data class UploadThesisTextRequest(
    @field:NotBlank(message = "Uploader ID is required")
    val uploaderId: String,

    @field:NotBlank(message = "File ID is required")
    val fileId: String,

    @field:NotBlank(message = "File name is required")
    val fileName: String
)

// Approve mentor thesis text request
data class ApproveMentorThesisTextRequest(
    @field:NotBlank(message = "Mentor ID is required")
    val mentorId: String,

    val isApproved: Boolean,

    val comments: String? = null
)

// Approve committee member thesis text request
data class ApproveCommitteeMemberThesisTextRequest(
    @field:NotBlank(message = "Member ID is required")
    val memberId: String,

    @field:NotBlank(message = "Member role is required")
    val memberRole: String,

    val isApproved: Boolean,

    val comments: String? = null
)

// Schedule thesis defense request
data class ScheduleThesisDefenseRequest(
    @field:NotBlank(message = "Scheduler ID is required")
    val schedulerId: String,

    @field:NotBlank(message = "Location is required")
    val location: String,

    @field:NotBlank(message = "Presentation date/time is required")
    val presentationDateTime: String
)

// Complete thesis defense request
data class CompleteThesisDefenseRequest(
    @field:NotBlank(message = "Mentor ID is required")
    val mentorId: String,

    val grade: Int,

    val comments: String? = null
)

// Cancel thesis request
data class CancelThesisRequest(
    @field:NotBlank(message = "Canceler ID is required")
    val cancelerId: String,

    @field:NotBlank(message = "Reason is required")
    val reason: String
)