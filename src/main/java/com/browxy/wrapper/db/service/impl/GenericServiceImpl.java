package com.browxy.wrapper.db.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.browxy.wrapper.db.DuplicatedRecordException;
import com.browxy.wrapper.db.repository.GenericRepository;
import com.browxy.wrapper.db.service.GenericService;

public abstract class GenericServiceImpl<T, E> implements GenericService<T, E> {
	protected final GenericRepository<T, E> repository;

	protected GenericServiceImpl(GenericRepository<T, E> repository) {
		this.repository = repository;
	}

	@Override
	public T getById(E id) throws SQLException {
		return repository.findById(id);
	}

	@Override
	public List<T> getAll() throws SQLException {
		return repository.findAll();
	}

	@Override
	public List<T> getMany(List<String> columns, Map<String, String> joins, Map<String, Map<String, String>> filters,
			Map<String, String> orderBy, boolean convertNumericToString, int limit, int offset) throws SQLException {
		return repository.findMany(columns, joins, filters, orderBy, convertNumericToString, limit, offset);
	}

    @Override
	public List<T> getByCustom(String query, List<String> columns, boolean convertNumericToString) throws SQLException {
		return repository.findByCustom(query, columns, convertNumericToString);
	}
	
	@Override
	public String create(T entity) throws SQLException, DuplicatedRecordException {
		return repository.save(entity);
	}

	@Override
	public boolean update(T entity) throws SQLException, NumberFormatException, DuplicatedRecordException {
		return repository.update(entity);
	}

	@Override
	public boolean delete(E id) throws SQLException {
		return repository.deleteById(id);
	}

	@Override
	public Long getTotalRows(String query, String key) throws SQLException, NumberFormatException {
		return repository.countRows(query, key);
	}
}
