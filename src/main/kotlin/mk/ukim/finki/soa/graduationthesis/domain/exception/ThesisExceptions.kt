package mk.ukim.finki.soa.graduationthesis.domain.exception

// Base exception for thesis domain
abstract class ThesisDomainException(message: String) : RuntimeException(message)

// Invalid thesis state transition
class InvalidThesisStateException(message: String) : ThesisDomainException(message)

// Invalid thesis data
class InvalidThesisDataException(message: String) : ThesisDomainException(message)

// Unauthorized operation
class UnauthorizedThesisOperationException(message: String) : ThesisDomainException(message)

// Thesis not found
class ThesisNotFoundException(thesisId: String) : ThesisDomainException("Thesis with ID $thesisId not found")

// File upload exception
class FileUploadException(message: String) : ThesisDomainException(message)

// Invalid grade exception
class InvalidGradeException(grade: Int) : ThesisDomainException("Grade $grade is invalid. Grade must be between 5 and 10")