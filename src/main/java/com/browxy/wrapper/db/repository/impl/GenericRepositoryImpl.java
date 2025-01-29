package com.browxy.wrapper.db.repository.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.browxy.wrapper.db.DBManager;
import com.browxy.wrapper.db.DuplicatedRecordException;
import com.browxy.wrapper.db.repository.GenericRepository;

public abstract class GenericRepositoryImpl<T, E> implements GenericRepository<T, E> {
	private final DBManager dbManager;
	private final String tableName;

	protected GenericRepositoryImpl(DBManager dbManager, String tableName) {
		this.dbManager = dbManager;
		this.tableName = tableName;
	}

	@Override
	public T findById(E id) throws SQLException {
		String query = "SELECT * FROM " + tableName + " WHERE id = ?";
		String[] parameters = { String.valueOf(id) };
		return mapRowToEntity(dbManager.genericSelectSingleRow("findById", query, parameters, false));
	}

	@Override
	public List<T> findAll() throws SQLException {
		String query = "SELECT * FROM " + tableName;
		return mapRowsToEntities(dbManager.genericSelect("findAll", query, null, false));
	}

	@Override
	public List<T> findMany(List<String> columns, Map<String, String> joins, Map<String, Map<String, String>> filters,
			Map<String, String> orderBy, boolean convertNumericToString, int limit, int offset) throws SQLException {
		return mapRowsToEntities(dbManager.selectWithFiltersAndJoins("selectMany", tableName, columns, joins, filters,
				orderBy, true, limit, offset));
	}
	
	@Override
	public List<T> findByCustom(String query, List<String> columns, boolean convertNumericToString)
			throws SQLException {
		String[] parameters = columns.stream().toArray(String[]::new);
		return mapRowsToEntities(dbManager.genericSelect("findByCustom", query, parameters, false));
	}

	@Override
	public String save(T entity) throws SQLException, DuplicatedRecordException {
		Map<String, String> values = mapEntityToValues(entity);
		return dbManager.genericInsert(null, "insert", tableName, values);
	}

	@Override
	public boolean update(T entity) throws NumberFormatException, SQLException, DuplicatedRecordException {
		Map<String, String> values = mapEntityToValues(entity);
		String result = dbManager.genericUpdate(null, "update", tableName, Long.parseLong(values.get("id")),
				mapValuesToRecord(values));
		return result == null;
	}

	@Override
	public boolean deleteById(E id) throws SQLException {
		return dbManager.genericDelete(tableName, (Long) id);
	}

	@Override
	public Long countRows(String query, String key) throws SQLException, NumberFormatException {
		String[] parameters = {};
		Map<String, String> rows = dbManager.genericSelectAsMap("select", query, parameters, false);
		return Long.parseLong(rows.get(key));
	}

	protected abstract T mapRowToEntity(Map<String, String> row);

	protected abstract List<T> mapRowsToEntities(List<Map<String, String>> rows);

	protected abstract Map<String, String> mapEntityToValues(T entity);

	protected abstract String[][] mapValuesToRecord(Map<String, String> values);
}
