package com.example.ever.hare.repository;

import com.example.ever.hare.entity.Option;
import com.example.ever.hare.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findOptionsByTheme(Theme theme);
}
