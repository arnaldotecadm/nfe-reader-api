package br.com.asoft.nfereader.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

//	private Log logger = LogFactory.getLog(GlobalControllerExceptionHandler.class);

	@ExceptionHandler({ ConversionFailedException.class })
	@ResponseBody
	public ResponseEntity<String> handleCustomException(ConversionFailedException ex) {
		if (ex.getMessage().contains("No enum constant")) {
			return new ResponseEntity<>(
					"Valor informado para o parâmetro não compatível com o valor informado, por favor consulte a documentação para maiores detalhes.",
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public ResponseEntity<CustomExceptionDTO> handleCustomSimpleException(ValidationException validationException) {
		HttpStatus responseStatus = validationException.getHttpStatus();
		return new ResponseEntity<>(new CustomExceptionDTO(validationException.getDescription()), responseStatus);
	}

	@ExceptionHandler({ SizeLimitExceededException.class })
	@ResponseBody
	public ResponseEntity<CustomExceptionDTO> handleVersioningException(Exception ve) {
		return new ResponseEntity<>(new CustomExceptionDTO(ve.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	

}