package mk.ukim.finki.soa.graduationthesis.presentation.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import mk.ukim.finki.soa.graduationthesis.application.service.ThesisService
import mk.ukim.finki.soa.graduationthesis.domain.exception.ThesisDomainException
import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisStatus
import mk.ukim.finki.soa.graduationthesis.infrastructure.persistence.ThesisView
import mk.ukim.finki.soa.graduationthesis.presentation.dto.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/thesis")
@Tag(name = "Thesis", description = "Thesis management endpoints")
class ThesisController(private val thesisService: ThesisService) {

    @PostMapping
    @Operation(summary = "Create a new thesis")
    fun createThesis(@Valid @RequestBody request: CreateThesisRequest): CompletableFuture<ResponseEntity<String>> {
        return thesisService.createThesis(
            title = request.title,
            area = request.area,
            description = request.description,
            studentId = request.studentId,
            mentorId = request.mentorId,
            firstMemberId = request.firstMemberId,
            secondMemberId = request.secondMemberId
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/accept")
    @Operation(summary = "Accept thesis proposal by student")
    fun acceptThesisProposal(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: AcceptThesisProposalRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.acceptThesisProposal(
            thesisId = thesisId,
            studentId = request.studentId
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/validate")
    @Operation(summary = "Validate thesis proposal by administration")
    fun validateThesisProposal(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: ValidateThesisProposalRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.validateThesisProposal(
            thesisId = thesisId,
            adminId = request.adminId,
            isValid = request.isValid,
            reason = request.reason
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/approve")
    @Operation(summary = "Approve thesis proposal by vice dean")
    fun approveThesisProposal(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: ApproveThesisProposalRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.approveThesisProposal(
            thesisId = thesisId,
            viceDeanId = request.viceDeanId,
            isApproved = request.isApproved,
            reason = request.reason
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/upload")
    @Operation(summary = "Upload thesis text")
    fun uploadThesisText(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: UploadThesisTextRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.uploadThesisText(
            thesisId = thesisId,
            uploaderId = request.uploaderId,
            fileId = request.fileId,
            fileName = request.fileName
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/approve-mentor")
    @Operation(summary = "Approve thesis text by mentor")
    fun approveMentorThesisText(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: ApproveMentorThesisTextRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.approveMentorThesisText(
            thesisId = thesisId,
            mentorId = request.mentorId,
            isApproved = request.isApproved,
            comments = request.comments
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/approve-committee")
    @Operation(summary = "Approve thesis text by committee member")
    fun approveCommitteeMemberThesisText(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: ApproveCommitteeMemberThesisTextRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.approveCommitteeMemberThesisText(
            thesisId = thesisId,
            memberId = request.memberId,
            memberRole = request.memberRole,
            isApproved = request.isApproved,
            comments = request.comments
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/schedule")
    @Operation(summary = "Schedule thesis defense")
    fun scheduleThesisDefense(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: ScheduleThesisDefenseRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.scheduleThesisDefense(
            thesisId = thesisId,
            schedulerId = request.schedulerId,
            location = request.location,
            presentationDateTime = request.presentationDateTime
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/complete")
    @Operation(summary = "Complete thesis defense")
    fun completeThesisDefense(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: CompleteThesisDefenseRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.completeThesisDefense(
            thesisId = thesisId,
            mentorId = request.mentorId,
            grade = request.grade,
            comments = request.comments
        ).thenApply { ResponseEntity.ok(it) }
    }

    @PostMapping("/{thesisId}/cancel")
    @Operation(summary = "Cancel thesis")
    fun cancelThesis(
        @PathVariable thesisId: String,
        @Valid @RequestBody request: CancelThesisRequest
    ): CompletableFuture<ResponseEntity<String>> {
        return thesisService.cancelThesis(
            thesisId = thesisId,
            cancelerId = request.cancelerId,
            reason = request.reason
        ).thenApply { ResponseEntity.ok(it) }
    }

    @GetMapping("/{thesisId}")
    @Operation(summary = "Get thesis by ID")
    fun getThesis(@PathVariable thesisId: String): ResponseEntity<ThesisView> {
        val thesis = thesisService.getThesis(thesisId)
        return if (thesis != null) {
            ResponseEntity.ok(thesis)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get theses by student ID")
    fun getThesesByStudent(@PathVariable studentId: String): List<ThesisView> {
        return thesisService.getThesesByStudent(studentId)
    }

    @GetMapping("/mentor/{mentorId}")
    @Operation(summary = "Get theses by mentor ID")
    fun getThesesByMentor(@PathVariable mentorId: String): List<ThesisView> {
        return thesisService.getThesesByMentor(mentorId)
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get theses by status")
    fun getThesesByStatus(@PathVariable status: String): ResponseEntity<List<ThesisView>> {
        return try {
            val thesisStatus = ThesisStatus.valueOf(status)
            ResponseEntity.ok(thesisService.getThesesByStatus(thesisStatus))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping
    @Operation(summary = "Get all theses")
    fun getAllTheses(): List<ThesisView> {
        return thesisService.getAllTheses()
    }

    @ExceptionHandler(ThesisDomainException::class)
    fun handleDomainException(e: ThesisDomainException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(e.message ?: "Domain error"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(e.message ?: "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    data class ErrorResponse(val message: String)
}