package ProjectAlkemy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.service.UploadImageServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/image")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Image Controller", description = "All Api Rest methods to manage images")
public class ImageController {

    @Autowired
    UploadImageServiceImp uploadImageServiceImp;


    @Operation(summary = "Image upload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "    Show image url..", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "    Internal Server Error..") })
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(@Parameter(description="	File to upload")@RequestParam("file") MultipartFile multipartFile) {
            String url = uploadImageServiceImp.uploadFile(multipartFile);
            return new ResponseEntity<>(new ResponseDto(200,url) , HttpStatus.OK);
    }

    @Operation(summary = "Image deletion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "    Show delete confirmation message..", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "    Internal Server Error..") })
    @DeleteMapping
    public ResponseEntity<?> deleteImage(@Parameter(description="	File Url to delete")@RequestParam ("url") String url) {
            String delete = uploadImageServiceImp.deleteFile(url);
            return new ResponseEntity<>(new ResponseDto(200,delete), HttpStatus.OK);
    }
}
