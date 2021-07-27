package com.siteminder.repository;

import com.siteminder.entity.EmailProvider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailProviderRepository extends CrudRepository<EmailProvider,Long> {

	List<EmailProvider> findByStatus(String status);
}
