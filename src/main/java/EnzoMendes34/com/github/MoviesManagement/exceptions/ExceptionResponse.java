package EnzoMendes34.com.github.MoviesManagement.exceptions;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {}