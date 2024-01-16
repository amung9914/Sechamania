package com.blog.repository;

import com.blog.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query("select c from Company c join fetch c.address a")
    List<Company> findAllForMap();

    @Query("select c from Company c join fetch c.address a where c.name = :name")
    List<Company> findAllByName(@Param("name")String name);


}
