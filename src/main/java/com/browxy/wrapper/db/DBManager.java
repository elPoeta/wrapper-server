package com.browxy.wrapper.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBManager {

	private static final int duplicateDBErrorCode = 1062;
	private static Logger logger = LoggerFactory.getLogger(DBManager.class);

	private static DBManager instance = null;
	private static final Object lock = new Object();

	private String userName;
	private String password;
	private String DBUrl;
	private String DBName;

	private GenericObjectPool<PoolableConnection> pool = null;
	private PoolingDataSource<PoolableConnection> dataSource = null;

	public DBManager(String userName, String password, String DBUrl, String dbName) {
		try {
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.userName = userName;
			this.password = password;
			this.DBUrl = DBUrl;
			this.DBName = dbName;

			ConnectionFactory cf = new DriverManagerConnectionFactory(this.DBUrl, this.userName, this.password);

			PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, null);
			pcf.setValidationQuery("SELECT 1");

			GenericObjectPoolConfig<PoolableConnection> config = new GenericObjectPoolConfig<>();
			config.setTestOnBorrow(true);
			config.setMaxTotal(10);
			pool = new GenericObjectPool<>(pcf, config);
			pcf.setPool(pool);
			dataSource = new PoolingDataSource<>(pool);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("error initializing dbManager", e1);
		}
	}

	public static DBManager getInstance(String userName, String password, String DBUrl, String dbName) {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new DBManager(userName, password, DBUrl, dbName);
				}
			}
		}
		return instance;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			logger.trace(
					"db before getting connection - NumActive: " + pool.getNumActive() + " NumIdle: "
							+ pool.getNumIdle() + " Thread: " + Thread.currentThread().getName(),
					new Exception("stacktrace"));
			conn = dataSource.getConnection();
			logger.trace("db after getting connection - NumActive: " + pool.getNumActive() + " NumIdle: "
					+ pool.getNumIdle() + " Thread: " + Thread.currentThread().getName(), new Exception("stacktrace"));
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("getConnection", e1);
		}
		return conn;
	}

	private void returnConnection(Connection conn) {
		try {
			logger.trace(
					"db before getting connection - NumActive: " + pool.getNumActive() + " NumIdle: "
							+ pool.getNumIdle() + " Thread: " + Thread.currentThread().getName(),
					new Exception("stacktrace"));
			conn.close();
			logger.trace("db after getting connection - NumActive: " + pool.getNumActive() + " NumIdle: "
					+ pool.getNumIdle() + " Thread: " + Thread.currentThread().getName(), new Exception("stacktrace"));
		} catch (SQLException e1) {

			logger.error("error returning connection", e1);
		}
	}

	public String genericSelectSingleValue(String operationName, String query, String[] parameters,
			boolean convertNumericToString) throws Exception {
		List<Map<String, String>> results = genericSelect(operationName, query, parameters, convertNumericToString);
		if (results.size() == 0) {
			return null;
		} else {
			return results.get(0).values().iterator().next();
		}
	}

	public List<String> genericSelectSingleColumn(String operationName, String query, String[] parameters,
			String columnName, boolean convertNumericToString) throws Exception {
		List<Map<String, String>> results = genericSelect("getLocationInterfaceIds", query, parameters,
				convertNumericToString);
		List<String> columnResults = new ArrayList<String>();
		for (Map<String, String> resultItem : results) {
			columnResults.add(resultItem.get(columnName));
		}
		return columnResults;
	}

	public Map<String, String> genericSelectSingleRow(String operationName, String query, String[] parameters,
			boolean convertNumericToString) throws SQLException {
		List<Map<String, String>> results = genericSelect(operationName, query, parameters, convertNumericToString);
		if (results.size() == 0) {
			return null;
		} else {
			return results.get(0);
		}
	}

	public Map<String, Object> genericSelectSingleRowUncasted(String operationName, String query, String[] parameters)
			throws Exception {
		List<Map<String, Object>> results = genericUncastedSelect(operationName, query, parameters);
		if (results.size() == 0) {
			return null;
		} else {
			return results.get(0);
		}
	}

	public Map<String, String> genericSelectAsMap(String operationName, String query, String[] parameters,
			boolean convertNumericToString, String keyName, String valueName) throws SQLException {
		List<Map<String, String>> results = genericSelect(operationName, query, parameters, convertNumericToString);
		Map<String, String> mapResult = new TreeMap<String, String>();
		for (Map<String, String> resultItem : results) {
			mapResult.put(resultItem.get(keyName), resultItem.get(valueName));
		}
		return mapResult;
	}

	public Map<String, String> genericSelectAsMap(String operationName, String query, String[] parameters,
			boolean convertNumericToString) throws SQLException {
		List<Map<String, String>> results = genericSelect(operationName, query, parameters, convertNumericToString);
		Map<String, String> mapResult = new TreeMap<String, String>();
		for (Map<String, String> resultItem : results) {
			resultItem.forEach((k, v) -> mapResult.put(k, v));
		}
		return mapResult;
	}

	public List<Map<String, String>> genericSelect(String operationName, String query, String[] parameters,
			boolean convertNumericToString) throws SQLException {
		Connection conn = null;
		PreparedStatement getResultsPreparedStatement = null;
		ResultSet results = null;
		List<Map<String, String>> mappedResults = new ArrayList<Map<String, String>>();
		try {
			conn = getConnection();
			getResultsPreparedStatement = conn.prepareStatement(query);
			for (int position = 0; parameters != null && position < parameters.length; position++) {
				if (isNumeric(parameters[position]) && convertNumericToString) {
					getResultsPreparedStatement.setLong(position + 1, Long.parseLong(parameters[position]));
				} else {
					getResultsPreparedStatement.setString(position + 1, parameters[position]);
				}
			}
			results = getResultsPreparedStatement.executeQuery();
			Set<String> columnNames = new LinkedHashSet<String>();
			for (int i = 1; i < results.getMetaData().getColumnCount() + 1; i++) {
				columnNames.add(results.getMetaData().getColumnName(i));
			}
			while (results.next()) {
				Map<String, String> item = new LinkedHashMap<String, String>();
				for (String columnName : columnNames) {
					item.put(columnName, results.getString(columnName));
				}
				mappedResults.add(item);
			}
		} finally {
			closeResources(results, getResultsPreparedStatement, null, conn);
			results = null;
			getResultsPreparedStatement = null;
			conn = null;
		}
		return mappedResults;
	}

	public boolean genericExecute(String operationName, String query, String[] parameters,
			boolean convertNumericToString) throws SQLException {
		Connection conn = null;
		PreparedStatement getResultsPreparedStatement = null;
		boolean results;
		try {
			conn = getConnection();
			getResultsPreparedStatement = conn.prepareStatement(query);
			for (int position = 0; parameters != null && position < parameters.length; position++) {
				if (isNumeric(parameters[position]) && convertNumericToString) {
					getResultsPreparedStatement.setLong(position + 1, Long.parseLong(parameters[position]));
				} else {
					getResultsPreparedStatement.setString(position + 1, parameters[position]);
				}
			}
			results = getResultsPreparedStatement.execute();
		} finally {
			closeResources(null, getResultsPreparedStatement, null, conn);
			getResultsPreparedStatement = null;
			conn = null;
		}
		return results;
	}

	private List<Map<String, Object>> genericUncastedSelect(String operationName, String query, String[] parameters)
			throws Exception {
		Connection conn = null;
		PreparedStatement getResultsPreparedStatement = null;
		ResultSet results = null;
		List<Map<String, Object>> mappedResults = new ArrayList<Map<String, Object>>();
		try {
			conn = getConnection();
			getResultsPreparedStatement = conn.prepareStatement(query);
			for (int position = 0; parameters != null && position < parameters.length; position++) {
				if (isNumeric(parameters[position])) {
					getResultsPreparedStatement.setLong(position + 1, Long.parseLong(parameters[position]));
				} else {
					getResultsPreparedStatement.setString(position + 1, parameters[position]);
				}
			}
			results = getResultsPreparedStatement.executeQuery();
			Set<String> columnNames = new LinkedHashSet<String>();
			for (int i = 1; i < results.getMetaData().getColumnCount() + 1; i++) {
				columnNames.add(results.getMetaData().getColumnName(i));
			}
			while (results.next()) {
				Map<String, Object> item = new LinkedHashMap<String, Object>();
				for (String columnName : columnNames) {
					item.put(columnName, results.getObject(columnName));
				}
				mappedResults.add(item);
			}

		} catch (Exception e1) {
			logger.debug("Could not execute query for operationName: " + operationName, e1);
			throw e1;
		} finally {
			closeResources(results, getResultsPreparedStatement, null, conn);
			results = null;
			getResultsPreparedStatement = null;
			conn = null;
		}
		return mappedResults;
	}

	public String genericStatement(Connection conn, String operationName, String query, String[] columnTypes,
			String[] columnValues) throws SQLException, DuplicatedRecordException {
		PreparedStatement preparedStatement = null;
		boolean connectionProvided = false;
		try {
			if (conn == null) {
				conn = getConnection();
				connectionProvided = true;
			}
			preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (int position = 0; columnTypes != null && position < columnTypes.length; position++) {
				if (columnTypes[position].equals("string")) {
					preparedStatement.setString(position + 1, columnValues[position]);
				}
				if (columnTypes[position].equals("int")) {
					preparedStatement.setInt(position + 1, Integer.parseInt(columnValues[position]));
				}
				if (columnTypes[position].equals("timestamp")) {
					preparedStatement.setTimestamp(position + 1, new Timestamp(System.currentTimeMillis()));
				}
			}
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet != null && resultSet.next()) {
				return String.valueOf(resultSet.getInt(1));
			}
		} catch (SQLException e2) {
			if ((e2.getErrorCode() == duplicateDBErrorCode)) {
				throw new DuplicatedRecordException();
			} else {
				throw e2;
			}
		} finally {
			if (connectionProvided) {
				closeResources(null, preparedStatement, null, conn);
			} else {
				closeResources(null, preparedStatement, null, null);
			}
		}
		return null;
	}

	public String genericInsert(Connection conn, String operationName, String table, Map<String, String> set)
			throws SQLException, DuplicatedRecordException {
		String keySet = "";
		String questionMarkSet = "";
		int i = 0;
		String[] columnTypes = new String[set.keySet().size()];
		String[] columnValues = new String[set.keySet().size()];
		for (String key : set.keySet()) {
			keySet += key + ",";
			questionMarkSet += "?,";
			columnTypes[i] = "string";
			columnValues[i] = set.get(key);
			i++;
		}
		keySet = keySet.substring(0, keySet.length() - 1);
		questionMarkSet = questionMarkSet.substring(0, questionMarkSet.length() - 1);
		String query = "INSERT INTO " + table + " (" + keySet + ") values (" + questionMarkSet + ")";
		return genericStatement(conn, operationName, query, columnTypes, columnValues);
	}

	public String genericUpdate(Connection conn, String operationName, String table, Long id, String[][] record)
			throws SQLException, DuplicatedRecordException {
		String keySet = "";
		int i = 0;
		String[] columnTypes = new String[record.length];
		String[] columnValues = new String[record.length];

		for (String[] data : record) {
			if (data[0].equals("id")) {
				continue;
			}
			keySet += data[0] + " = ?,";
			columnTypes[i] = "string";
			columnValues[i] = data[1];
			i++;
		}
		columnTypes[i] = "long";
		columnValues[i] = String.valueOf(id);

		keySet = keySet.substring(0, keySet.length() - 1);
		String query = "UPDATE " + table + " SET " + keySet + " WHERE id = ?";
		return genericStatement(null, "update", query, columnTypes, columnValues);
	}

	public boolean genericDelete(String table, Long id) throws SQLException {
		String query = "DELETE FROM " + table + " WHERE id = ?";
		String[] parameters = { String.valueOf(id) };
		return genericExecute("delete", query, parameters, false);
	}

	public List<Map<String, String>> selectWithFiltersAndJoins(String operationName, String baseTable,
			List<String> columns, Map<String, String> joins, Map<String, Map<String, String>> filters,
			Map<String, String> orderBy, boolean convertNumericToString, int limit, int offset) throws SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Map<String, String>> mappedResults = new ArrayList<>();
		try {
			conn = getConnection();
			StringBuilder queryBuilder = new StringBuilder("SELECT ");

			// Add columns if present, otherwise select all columns
			if (columns != null && !columns.isEmpty()) {
				queryBuilder.append(String.join(", ", columns));
			} else {
				queryBuilder.append("*");
			}

			queryBuilder.append(" FROM ").append(baseTable);

			// Add joins if present
			if (joins != null && !joins.isEmpty()) {
				for (Map.Entry<String, String> join : joins.entrySet()) {
					queryBuilder.append(" JOIN ").append(join.getKey()).append(" ON ").append(join.getValue());
				}
			}

			// Add filters if present
			if (filters != null && !filters.isEmpty()) {
				queryBuilder.append(" WHERE ");
				int filterIndex = 0;
				for (Map.Entry<String, Map<String, String>> filter : filters.entrySet()) {
					if (filterIndex > 0) {
						queryBuilder.append(" AND ");
					}
					String columnName = filter.getKey();
					Map<String, String> conditionValue = filter.getValue();
					String condition = conditionValue.get("condition");
					//String value = conditionValue.get("value");
					queryBuilder.append(columnName).append(" ").append(condition).append(" ?");
					filterIndex++;
				}
			}

			// Add ORDER BY if present
			if (orderBy != null && !orderBy.isEmpty()) {
				queryBuilder.append(" ORDER BY ");
				int orderIndex = 0;
				for (Map.Entry<String, String> orderEntry : orderBy.entrySet()) {
					if (orderIndex > 0) {
						queryBuilder.append(", ");
					}
					queryBuilder.append(orderEntry.getKey()).append(" ").append(orderEntry.getValue());
					orderIndex++;
				}
			}

			// Add pagination
			queryBuilder.append(" LIMIT ? OFFSET ?");

			preparedStatement = conn.prepareStatement(queryBuilder.toString());

			// Set filter values in the prepared statement
			int paramIndex = 1;
			if (filters != null) {
				for (Map.Entry<String, Map<String, String>> filter : filters.entrySet()) {
					String value = filter.getValue().get("value");
					if (isNumeric(value) && convertNumericToString) {
						preparedStatement.setLong(paramIndex++, Long.parseLong(value));
					} else {
						preparedStatement.setString(paramIndex++, value);
					}
				}
			}

			// Set pagination parameters
			preparedStatement.setInt(paramIndex++, limit);
			preparedStatement.setInt(paramIndex, offset);

			resultSet = preparedStatement.executeQuery();
			Set<String> columnNames = new LinkedHashSet<>();
			for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
				columnNames.add(resultSet.getMetaData().getColumnName(i));
			}
			while (resultSet.next()) {
				Map<String, String> row = new LinkedHashMap<>();
				for (String columnName : columnNames) {
					row.put(columnName, resultSet.getString(columnName));
				}
				mappedResults.add(row);
			}
		} finally {
			closeResources(resultSet, preparedStatement, null, conn);
		}
		return mappedResults;
	}

	private static boolean isNumeric(String s) {
		if (s == null) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) < '0' || s.charAt(i) > '9') {
				return false;
			}
		}
		return true;
	}

	public void performTransaction(List<Runnable> operations) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);

			for (Runnable operation : operations) {
				operation.run();
			}

			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			throw e;
		} finally {
			if (conn != null) {
				closeResources(null, null, null, conn);
			}
		}
	}

	private void closeResources(ResultSet rset, PreparedStatement pstm, CallableStatement stmt, Connection conn) {
		closeResources(rset, pstm, stmt, null, conn);
	}

	private void closeResources(ResultSet rset, PreparedStatement pstm, CallableStatement stmt, Statement stat,
			Connection conn) {
		closeResources(rset, pstm, stmt, stat, conn, false);
	}

	private void closeResources(ResultSet rset, PreparedStatement pstm, CallableStatement stmt, Statement stat,
			Connection conn, boolean reallyCloseConnection) {
		if (rset != null) {
			try {
				rset.close();
			} catch (java.sql.SQLException e1) {
				logger.error("error closing resources - closing rset", e1);
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (java.sql.SQLException e1) {
				logger.error("error closing resources - closing stmt", e1);
			}
		}
		if (pstm != null) {
			try {
				pstm.close();
			} catch (java.sql.SQLException e1) {
				logger.error("error closing resources - closing pstmt", e1);
			}
		}
		if (stat != null) {
			try {
				stat.close();
			} catch (java.sql.SQLException e1) {
				logger.error("error closing resources - closing stat", e1);
			}
		}
		if (conn != null) {
			if (!reallyCloseConnection) {
				returnConnection(conn);
			} else {
				try {
					new PoolingDriver().closePool(this.DBName);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
