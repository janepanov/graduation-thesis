package mk.ukim.finki.soa.graduationthesis.application.service

import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisStatus
import mk.ukim.finki.soa.graduationthesis.infrastructure.persistence.ThesisView
import java.util.concurrent.CompletableFuture

interface ThesisService {
    fun createThesis(
        title: String,
        area: String,
        description: String,
        studentId: String,
        mentorId: String,
        firstMemberId: String,
        secondMemberId: String
    ): CompletableFuture<String>

    fun acceptThesisProposal(thesisId: String, studentId: String): CompletableFuture<String>

    fun validateThesisProposal(
        thesisId: String,
        adminId: String,
        isValid: Boolean,
        reason: String?
    ): CompletableFuture<String>

    fun approveThesisProposal(
        thesisId: String,
        viceDeanId: String,
        isApproved: Boolean,
        reason: String?
    ): CompletableFuture<String>

    fun uploadThesisText(
        thesisId: String,
        uploaderId: String,
        fileId: String,
        fileName: String
    ): CompletableFuture<String>

    fun approveMentorThesisText(
        thesisId: String,
        mentorId: String,
        isApproved: Boolean,
        comments: String?
    ): CompletableFuture<String>

    fun approveCommitteeMemberThesisText(
        thesisId: String,
        memberId: String,
        memberRole: String,
        isApproved: Boolean,
        comments: String?
    ): CompletableFuture<String>

    fun scheduleThesisDefense(
        thesisId: String,
        schedulerId: String,
        location: String,
        presentationDateTime: String
    ): CompletableFuture<String>

    fun completeThesisDefense(
        thesisId: String,
        mentorId: String,
        grade: Int,
        comments: String?
    ): CompletableFuture<String>

    fun cancelThesis(
        thesisId: String,
        cancelerId: String,
        reason: String
    ): CompletableFuture<String>

    fun getThesis(thesisId: String): ThesisView?

    fun getThesesByStudent(studentId: String): List<ThesisView>

    fun getThesesByMentor(mentorId: String): List<ThesisView>

    fun getThesesByStatus(status: ThesisStatus): List<ThesisView>

    fun getAllTheses(): List<ThesisView>
}