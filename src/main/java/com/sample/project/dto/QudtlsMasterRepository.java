package com.sample.project.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.project.entity.QudtlsMaster;

@Repository
public interface QudtlsMasterRepository extends JpaRepository<QudtlsMaster, Long> {

}
