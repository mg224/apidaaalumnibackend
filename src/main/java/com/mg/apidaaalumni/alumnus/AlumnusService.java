package com.mg.apidaaalumni.alumnus;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AlumnusService {
    private final AlumnusRepository alumnusRepository;

    @Autowired
    public AlumnusService(AlumnusRepository alumnusRepository) {
        this.alumnusRepository = alumnusRepository;
    }

    public List<Alumnus> getAllAlums() {
        return alumnusRepository.findAll();
    }

    public Alumnus getAlumnusById(Integer alumnusId) {
        return alumnusRepository.findById(alumnusId).orElse(null);
    }

    public List<Alumnus> getAlumsByIndustry(String searchText) {
        return alumnusRepository.findAll().stream()
                .filter(alumnus -> alumnus.getIndustry().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Alumnus> getAlumsByName(String searchText) {
        return alumnusRepository.findAll().stream()
                .filter(alumnus -> alumnus.getName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Alumnus> getAlumsByYear(Integer year) {
        return alumnusRepository.findAll().stream()
                .filter(alumnus -> year.equals(alumnus.getYear()))
                .collect(Collectors.toList());
    }

    public List<Alumnus> getAlumsByPastRoles(String searchText) {
        return alumnusRepository.findAll().stream()
                .filter(alumnus -> alumnus.getPastRoles().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Alumnus addAlumnus(Alumnus alumnus) {
        alumnusRepository.save(alumnus);
        return alumnus;
    }

    public Alumnus updateAlumnus(Alumnus updatedAlumnus) {
        Optional<Alumnus> existingAlum = alumnusRepository.findById(updatedAlumnus.getAlumnusId());

        if (existingAlum.isPresent()) {
            Alumnus alumToUpdate = existingAlum.get();
            alumToUpdate.setAlumnusId(updatedAlumnus.getAlumnusId());
            alumToUpdate.setName(updatedAlumnus.getName());
            alumToUpdate.setPronouns(updatedAlumnus.getPronouns());
            alumToUpdate.setEmail(updatedAlumnus.getEmail());
            alumToUpdate.setHeadshot(updatedAlumnus.getHeadshot());
            alumToUpdate.setYear(updatedAlumnus.getYear());
            alumToUpdate.setPastRoles(updatedAlumnus.getPastRoles());
            alumToUpdate.setIndustry(updatedAlumnus.getIndustry());

            alumnusRepository.save(alumToUpdate);
            return alumToUpdate;
        }
        return null;
    }

    public Alumnus updateHeadshot(Integer alumnusId, String filename) {
        Optional<Alumnus> existingAlum = alumnusRepository.findById(alumnusId);

        if (existingAlum.isPresent()) {
            Alumnus alumToUpdate = existingAlum.get();
            alumToUpdate.setHeadshot(filename);

            alumnusRepository.save(alumToUpdate);
            return alumToUpdate;
        }
        return null;
    }

    @Transactional
    public void deleteAlumnus(Integer alumnusId) {
        alumnusRepository.deleteById(alumnusId);
    }

}
