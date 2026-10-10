package com.villageshare.backend.controller;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.villageshare.backend.entity.Resource;
import com.villageshare.backend.repository.BookingRepository;
import com.villageshare.backend.repository.ResourceRepository;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "http://localhost:5173")
public class ResourceController {

    private final ResourceRepository resourceRepository;
    private final BookingRepository bookingRepository;

    public ResourceController(
            ResourceRepository resourceRepository,
            BookingRepository bookingRepository) {

        this.resourceRepository = resourceRepository;
        this.bookingRepository = bookingRepository;
    }

    // Get all resources
    @GetMapping
    public List<Resource> getAllResources() {

        return resourceRepository.findAll();
    }

    // Get resources of a particular owner
    @GetMapping("/owner/{email}")
    public List<Resource> getResourcesByOwner(
            @PathVariable String email) {

        return resourceRepository.findByOwnerEmail(email);
    }

    // Add resource
    @PostMapping
    public Resource addResource(
            @RequestBody Resource resource) {

        return resourceRepository.save(resource);
    }
    @PutMapping("/{id}")
public Resource updateResource(
        @PathVariable Long id,
        @RequestBody Resource updatedResource) {

    Resource resource = resourceRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Resource not found"));

    resource.setName(updatedResource.getName());
    resource.setCategory(updatedResource.getCategory());
    resource.setLocation(updatedResource.getLocation());
    resource.setPrice(updatedResource.getPrice());

    return resourceRepository.save(resource);
}

    // Delete resource
    @DeleteMapping("/{id}")
    public void deleteResource(
            @PathVariable Long id) {

        if (!resourceRepository.existsById(id)) {
            throw new RuntimeException(
                    "Resource not found"
            );
        }

        // Check whether this resource has any booking
        if (bookingRepository.existsByResourceId(id)) {

            throw new RuntimeException(
                    "This resource cannot be deleted because it has existing bookings."
            );
        }

        resourceRepository.deleteById(id);
    }
}
