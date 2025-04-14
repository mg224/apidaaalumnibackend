package com.mg.apidaaalumni.alumnus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/alumnus")
public class AlumnusController {

    private final AlumnusService alumnusService;
    private final Path imageStoragePath = Paths.get("src/main/resources/static/headshots");

    @Autowired
    public AlumnusController(AlumnusService alumnusService) {
        this.alumnusService = alumnusService;
        // Ensure directory exists
        try {
            Files.createDirectories(imageStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @GetMapping
    public List<Alumnus> getAllAlums(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String pastRoles,
            @RequestParam(required = false) String industry) {

        if (name != null) {
            return alumnusService.getAlumsByName(name);
        }
        else if (year != null) {
            return alumnusService.getAlumsByYear(year);
        }
        else if (pastRoles != null) {
            return alumnusService.getAlumsByPastRoles(pastRoles);
        }
        else if (industry != null) {
            return alumnusService.getAlumsByIndustry(industry);
        }
        else {
            return alumnusService.getAllAlums();
        }
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path file = imageStoragePath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Alumnus> addAlumnus(@RequestBody Alumnus alumnus) {
        Alumnus createdAlumnus = alumnusService.addAlumnus(alumnus);
        return new ResponseEntity<>(createdAlumnus, HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("alumnusId") Integer alumnusId) {
        try {
            // Get file extension
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // Generate a unique filename
            String filename = alumnusId + "_" + UUID.randomUUID().toString() + extension;

            // Save the file
            Path targetLocation = imageStoragePath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Update the alumnus record with the new image filename
            alumnusService.updateHeadshot(alumnusId, filename);

            return ResponseEntity.ok().body(filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload the image: " + e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Alumnus> updateAlumnus(@RequestBody Alumnus alumnus) {
        Alumnus updatedAlumnus = alumnusService.updateAlumnus(alumnus);

        if (updatedAlumnus != null) {
            return new ResponseEntity<>(updatedAlumnus, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @DeleteMapping("/{alumnusId}")
//    public ResponseEntity<String> deleteAlumnus(@PathVariable Integer alumnusId) {
//        alumnusService.deleteAlumnus(alumnusId);
//        return new ResponseEntity<>("Alumnus deleted successfully", HttpStatus.OK);
//    }

    @DeleteMapping("/{alumnusId}")
    public ResponseEntity<String> deleteAlumnus(@PathVariable Integer alumnusId) {
        // Get alumnus to find image filename before deletion
        Alumnus alumnus = alumnusService.getAlumnusById(alumnusId);
        if (alumnus != null && alumnus.getHeadshot() != null) {
            // Delete the image file if it exists
            try {
                Path imagePath = imageStoragePath.resolve(alumnus.getHeadshot());
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                // Log error but continue with alumnus deletion
                System.err.println("Failed to delete image: " + e.getMessage());
            }
        }

        alumnusService.deleteAlumnus(alumnusId);
        return new ResponseEntity<>("Alumnus deleted successfully", HttpStatus.OK);
    }

}
