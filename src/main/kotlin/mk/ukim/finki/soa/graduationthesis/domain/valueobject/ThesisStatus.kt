package mk.ukim.finki.soa.graduationthesis.domain.valueobject

enum class ThesisStatus {
    DRAFT,                              // Initial state when thesis is created by professor
    STUDENT_APPROVAL_PENDING,           // Waiting for student to accept the thesis
    STUDENT_APPROVED,                   // Student has accepted the thesis
    ADMINISTRATION_VALIDATION_PENDING,  // Waiting for administration validation
    ADMINISTRATION_VALIDATED,           // Administration has validated the thesis
    VICE_DEAN_APPROVAL_PENDING,         // Waiting for vice dean approval
    VICE_DEAN_APPROVED,                 // Vice dean has approved the thesis
    THESIS_TEXT_PENDING,                // Waiting for thesis text to be uploaded
    MENTOR_APPROVAL_PENDING,            // Waiting for mentor to approve the thesis text
    MENTOR_APPROVED,                    // Mentor has approved the thesis text
    COMMITTEE_APPROVAL_PENDING,         // Waiting for committee members to approve
    COMMITTEE_APPROVED,                 // Committee members have approved
    DEFENSE_SCHEDULING_PENDING,         // Waiting for defense to be scheduled
    DEFENSE_SCHEDULED,                  // Defense has been scheduled
    DEFENSE_COMPLETED,                  // Defense has been completed
    GRADING_PENDING,                    // Waiting for grade to be assigned
    GRADED,                             // Grade has been assigned
    ARCHIVED,                           // Thesis has been archived
    REJECTED,                           // Thesis has been rejected
    CANCELED                            // Thesis has been canceled
}