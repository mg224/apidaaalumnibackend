package com.mg.apidaaalumni.alumnus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumnusRepository extends JpaRepository<Alumnus, Integer> {

    void deleteById(Integer alumnusId);
    Optional<Alumnus> findById(Integer id);

}
