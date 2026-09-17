package com.cinemaxpress.repository;


import com.cinemaxpress.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    List<Theatre> findByCityId(Long cityId);
    List<Theatre> findByManagerId(Long managerId);
}