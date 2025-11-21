package com.example.SpringJdbcCustomer.dao;

import com.example.SpringJdbcCustomer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCustomerDao implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper rowMapper = new CustomerRowMapper();

    private static final String INSERT_SQL = "INSERT INTO customer (full_name, email, social_security_number) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM customer WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE customer SET full_name = ?, email = ?, social_security_number = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM customer WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM customer";

    @Autowired
    public JdbcCustomerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Customer save(Customer customer) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"id"});
            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getSocialSecurityNumber());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            customer.setId(key.longValue());
        }
        return customer;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public int update(Customer customer) {
        return jdbcTemplate.update(UPDATE_SQL,
                customer.getFullName(),
                customer.getEmail(),
                customer.getSocialSecurityNumber(),
                customer.getId());
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }
}