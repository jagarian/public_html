package neo.db;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 	@Class Name	: 	DBUtil.java
 * 	@파일설명		: 	DatabaseMetaData 클래스를 활용한 데이터베이스 사이드 환경 정보를 얻는다.
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
 **********************************************************************************************             
 */
public class DBUtil {
	public static String toStringMetadata(DatabaseMetaData dbMetaData, String endDelimitor) {
		String addStr = "";
		try {
			addStr += "> getCatalogSeparator() : ["+dbMetaData.getCatalogSeparator()+"] => Retrieves the String that this database uses as the separator between a catalog and table name."+endDelimitor;		   
			addStr += "> getCatalogSeparator() : ["+dbMetaData.getCatalogSeparator()+"] => Retrieves the String that this database uses as the separator between a catalog and table name."+endDelimitor;
	        addStr += "> getCatalogTerm() : ["+dbMetaData.getCatalogTerm()+"] => Retrieves the database vendor's preferred term for 'catalog'."+endDelimitor;           
	        addStr += "> getDatabaseProductName() : ["+dbMetaData.getDatabaseProductName()+"] => Retrieves the name of this database product."+endDelimitor;           
	        addStr += "> getDatabaseProductVersion() : ["+dbMetaData.getDatabaseProductVersion()+"] => Retrieves the minor version number of the underlying database."+endDelimitor;           
	        addStr += "> getDefaultTransactionIsolation() : ["+dbMetaData.getDefaultTransactionIsolation()+"] => Retrieves this database's default transaction isolation level."+endDelimitor;           
	        addStr += "> getDriverMajorVersion() : ["+dbMetaData.getDriverMajorVersion()+"] => Retrieves this JDBC driver's major version number."+endDelimitor;           
	        addStr += "> getDriverMinorVersion() : ["+dbMetaData.getDriverMinorVersion()+"] => Retrieves this JDBC driver's minor version number."+endDelimitor;           
	        addStr += "> getDriverName() : ["+dbMetaData.getDriverName()+"] => Retrieves the name of this JDBC driver."+endDelimitor;           
	        addStr += "> getDriverVersion() : ["+dbMetaData.getDriverVersion()+"] => Retrieves the name of this JDBC driver."+endDelimitor;           
	        //addStr += "> getExtraNameCharacters() : ["+dbMetaData.getExtraNameCharacters()+"] => "+endDelimitor;           
	        //addStr += "> getIdentifierQuoteString() : ["+dbMetaData.getIdentifierQuoteString()+"] => "+endDelimitor;            
	        //addStr += "> getJDBCMajorVersion() : ["+dbMetaData.getJDBCMajorVersion()+"] => "+endDelimitor;           
	        //addStr += "> getJDBCMinorVersion() : ["+dbMetaData.getJDBCMinorVersion()+"] => "+endDelimitor;           
	        addStr += "> getMaxBinaryLiteralLength() : ["+dbMetaData.getMaxBinaryLiteralLength()+"] => Retrieves the maximum number of hex characters this database allows in an inline binary literal."+endDelimitor;           
	        addStr += "> getMaxCatalogNameLength() : ["+dbMetaData.getMaxCatalogNameLength()+"] => Retrieves the maximum number of characters that this database allows in a catalog name."+endDelimitor;           
	        addStr += "> getMaxCharLiteralLength() : ["+dbMetaData.getMaxCharLiteralLength()+"] => Retrieves the maximum number of characters this database allows for a character literal."+endDelimitor;           
	        addStr += "> getMaxColumnNameLength() : ["+dbMetaData.getMaxColumnNameLength()+"] => Retrieves the maximum number of characters this database allows for a column name."+endDelimitor;           
	        addStr += "> getMaxColumnsInGroupBy() : ["+dbMetaData.getMaxColumnsInGroupBy()+"] => Retrieves the maximum number of columns this database allows in a GROUP BY clause."+endDelimitor;           
	        addStr += "> getMaxColumnsInIndex() : ["+dbMetaData.getMaxColumnsInIndex()+"] => Retrieves the maximum number of columns this database allows in an index."+endDelimitor;           
	        addStr += "> getMaxColumnsInOrderBy() : ["+dbMetaData.getMaxColumnsInOrderBy()+"] => Retrieves the maximum number of columns this database allows in an ORDER BY clause."+endDelimitor;           
	        addStr += "> getMaxColumnsInSelect() : ["+dbMetaData.getMaxColumnsInSelect()+"] => "+endDelimitor;           
	        addStr += "> getMaxConnections() : ["+dbMetaData.getMaxConnections()+"] => Retrieves the maximum number of columns this database allows in a SELECT list."+endDelimitor;           
	        addStr += "> getMaxCursorNameLength() : ["+dbMetaData.getMaxCursorNameLength()+"] => "+endDelimitor;           
	        addStr += "> getMaxIndexLength() : ["+dbMetaData.getMaxIndexLength()+"] => Retrieves the maximum number of characters that this database allows in a cursor name."+endDelimitor;           
	        addStr += "> getMaxProcedureNameLength() : ["+dbMetaData.getMaxProcedureNameLength()+"] => Retrieves the maximum number of characters that this database allows in a procedure name."+endDelimitor;           
	        addStr += "> getMaxRowSize() : ["+dbMetaData.getMaxRowSize()+"] => Retrieves the maximum number of bytes this database allows in a single row."+endDelimitor;           
	        addStr += "> getMaxSchemaNameLength() : ["+dbMetaData.getMaxSchemaNameLength()+"] => Retrieves the maximum number of characters that this database allows in a schema name."+endDelimitor;           
	        addStr += "> getMaxStatementLength() : ["+dbMetaData.getMaxStatementLength()+"] => Retrieves the maximum number of characters this database allows in an SQL statement."+endDelimitor;          
	        addStr += "> getMaxStatements() : ["+dbMetaData.getMaxStatements()+"] => Retrieves the maximum number of active statements to this database that can be open at the same time."+endDelimitor;           
	        addStr += "> getMaxTableNameLength() : ["+dbMetaData.getMaxTableNameLength()+"] => Retrieves the maximum number of characters this database allows in a table name."+endDelimitor;           
	        addStr += "> getMaxTablesInSelect() : ["+dbMetaData.getMaxTablesInSelect()+"] => Retrieves the maximum number of tables this database allows in a SELECT statement."+endDelimitor;           
	        addStr += "> getMaxUserNameLength() : ["+dbMetaData.getMaxUserNameLength()+"] => Retrieves the maximum number of characters this database allows in a user name."+endDelimitor;           
	        addStr += "> getNumericFunctions() : ["+dbMetaData.getNumericFunctions()+"] => Retrieves a comma-separated list of math functions available with this database."+endDelimitor;           
	        //addStr += "> getProcedureTerm() : ["+dbMetaData.getProcedureTerm()+"] => "+endDelimitor;           
	        //addStr += "> getResultSetHoldability() : ["+dbMetaData.getResultSetHoldability()+"] => "+endDelimitor;   
	        addStr += "> getSchemaTerm() : ["+dbMetaData.getSchemaTerm()+"] => Retrieves the database vendor's preferred term for 'schema'."+endDelimitor;           
	        addStr += "> getSearchStringEscape() : ["+dbMetaData.getSearchStringEscape()+"] => Retrieves the string that can be used to escape wildcard characters."+endDelimitor;           
	        addStr += "> getSQLKeywords() : ["+dbMetaData.getSQLKeywords()+"] => Retrieves a comma-separated list of all of this database's SQL keywords that are NOT also SQL92 keywords."+endDelimitor;           
	        //addStr += "> getSQLStateType() : ["+dbMetaData.getSQLStateType()+"] => "+endDelimitor;           
	        addStr += "> getStringFunctions() : ["+dbMetaData.getStringFunctions()+"] => Retrieves a comma-separated list of string functions available with this database."+endDelimitor;           
	        addStr += "> getSystemFunctions() : ["+dbMetaData.getSystemFunctions()+"] => Retrieves a comma-separated list of system functions available with this database."+endDelimitor;           
	        addStr += "> getTimeDateFunctions() : ["+dbMetaData.getTimeDateFunctions()+"] => Retrieves a comma-separated list of the time and date functions available with this database."+endDelimitor;           
	        addStr += "> getURL() : ["+dbMetaData.getURL()+"] => Retrieves the URL for this DBMS."+endDelimitor;           
	        addStr += "> getUserName() : ["+dbMetaData.getUserName()+"] => Retrieves the user name as known to this database."+endDelimitor;           
	        addStr += "> isCatalogAtStart() : ["+dbMetaData.isCatalogAtStart()+"] => "+endDelimitor;           
	        addStr += "> isReadOnly() : ["+dbMetaData.isReadOnly()+"] => "+endDelimitor;           
	        //addStr += "> locatorsUpdateCopy() : ["+dbMetaData.locatorsUpdateCopy()+"] => "+endDelimitor;           
	        addStr += "> nullPlusNonNullIsNull() : ["+dbMetaData.nullPlusNonNullIsNull()+"] => Retrieves whether this database supports concatenations between NULL and non-NULL values being NULL."+endDelimitor;           
	        addStr += "> nullsAreSortedAtEnd() : ["+dbMetaData.nullsAreSortedAtEnd()+"] => Retrieves whether NULL values are sorted at the end regardless of sort order."+endDelimitor;           
	        addStr += "> nullsAreSortedAtStart() : ["+dbMetaData.nullsAreSortedAtStart()+"] => Retrieves whether NULL values are sorted at the start regardless of sort order."+endDelimitor;           
	        addStr += "> nullsAreSortedHigh() : ["+dbMetaData.nullsAreSortedHigh()+"] => Retrieves whether NULL values are sorted high."+endDelimitor;           
	        addStr += "> nullsAreSortedLow() : ["+dbMetaData.nullsAreSortedLow()+"] => Retrieves whether NULL values are sorted low."+endDelimitor;           
	        addStr += "> storesLowerCaseIdentifiers() : ["+dbMetaData.storesLowerCaseIdentifiers()+"] => Retrieves whether this database treats mixed case unquoted SQL identifiers as case insensitive and stores them in lower case"+endDelimitor;           
	        addStr += "> storesLowerCaseQuotedIdentifiers() : ["+dbMetaData.storesLowerCaseQuotedIdentifiers()+"] => Retrieves whether this database treats mixed case quoted SQL identifiers as case insensitive and stores them in lower case."+endDelimitor;           
	        addStr += "> storesMixedCaseIdentifiers() : ["+dbMetaData.storesMixedCaseIdentifiers()+"] => Retrieves whether this database treats mixed case unquoted SQL identifiers as case insensitive and stores them in mixed case."+endDelimitor;           
	        addStr += "> storesMixedCaseQuotedIdentifiers() : ["+dbMetaData.storesMixedCaseQuotedIdentifiers()+"] => Retrieves whether this database treats mixed case quoted SQL identifiers as case insensitive and stores them in mixed case."+endDelimitor;           
	        addStr += "> storesUpperCaseIdentifiers() : ["+dbMetaData.storesUpperCaseIdentifiers()+"] => Retrieves whether this database treats mixed case unquoted SQL identifiers as case insensitive and stores them in upper case."+endDelimitor;           
	        addStr += "> storesUpperCaseQuotedIdentifiers() : ["+dbMetaData.storesUpperCaseQuotedIdentifiers()+"] => Retrieves whether this database treats mixed case quoted SQL identifiers as case insensitive and stores them in upper case."+endDelimitor;           
	        addStr += "> supportsAlterTableWithAddColumn() : ["+dbMetaData.supportsAlterTableWithAddColumn()+"] => Retrieves whether this database supports ALTER TABLE with add column."+endDelimitor;           
	        addStr += "> supportsAlterTableWithDropColumn() : ["+dbMetaData.supportsAlterTableWithDropColumn()+"] => Retrieves whether this database supports ALTER TABLE with drop column."+endDelimitor;           
	        addStr += "> supportsANSI92EntryLevelSQL() : ["+dbMetaData.supportsANSI92EntryLevelSQL()+"] => Retrieves whether this database supports the ANSI92 entry level SQL grammar."+endDelimitor;           
	        addStr += "> supportsANSI92FullSQL() : ["+dbMetaData.supportsANSI92FullSQL()+"] => Retrieves whether this database supports the ANSI92 full SQL grammar supported."+endDelimitor;           
	        addStr += "> supportsBatchUpdates() : ["+dbMetaData.supportsBatchUpdates()+"] => Retrieves whether this database supports batch updates."+endDelimitor;           
	        addStr += "> supportsCatalogsInDataManipulation() : ["+dbMetaData.supportsCatalogsInDataManipulation()+"] => Retrieves whether a catalog name can be used in a data manipulation statement."+endDelimitor;           
	        addStr += "> supportsCatalogsInIndexDefinitions() : ["+dbMetaData.supportsCatalogsInIndexDefinitions()+"] => Retrieves whether a catalog name can be used in an index definition statement."+endDelimitor;           
	        addStr += "> supportsCatalogsInPrivilegeDefinitions() : ["+dbMetaData.supportsCatalogsInPrivilegeDefinitions()+"] => Retrieves whether a catalog name can be used in a privilege definition statement."+endDelimitor;           
	        addStr += "> supportsCatalogsInProcedureCalls() : ["+dbMetaData.supportsCatalogsInProcedureCalls()+"] => Retrieves whether a catalog name can be used in a procedure call statement."+endDelimitor;           
	        addStr += "> supportsColumnAliasing() : ["+dbMetaData.supportsColumnAliasing()+"] => Retrieves whether this database supports column aliasing."+endDelimitor;           
	        addStr += "> supportsConvert() : ["+dbMetaData.supportsConvert()+"] => Retrieves whether this database supports the CONVERT for two given SQL types."+endDelimitor;           
	        addStr += "> supportsCoreSQLGrammar() : ["+dbMetaData.supportsCoreSQLGrammar()+"] => Retrieves whether this database supports the ODBC Core SQL grammar."+endDelimitor;           
	        addStr += "> supportsCorrelatedSubqueries() : ["+dbMetaData.supportsCorrelatedSubqueries()+"] => Retrieves whether this database supports correlated subqueries."+endDelimitor;           
	        addStr += "> supportsDataDefinitionAndDataManipulationTransactions() : ["+dbMetaData.supportsDataDefinitionAndDataManipulationTransactions()+"] => Retrieves whether this database supports both data definition and data manipulation statements within a transaction."+endDelimitor;           
	        addStr += "> supportsDataManipulationTransactionsOnly() : ["+dbMetaData.supportsDataManipulationTransactionsOnly()+"] => Retrieves whether this database supports only data manipulation statements within a transaction."+endDelimitor;           
	        addStr += "> supportsDifferentTableCorrelationNames() : ["+dbMetaData.supportsDifferentTableCorrelationNames()+"] => Retrieves whether, when table correlation names are supported, they are restricted to being different from the names of the tables."+endDelimitor;           
	        addStr += "> supportsExpressionsInOrderBy() : ["+dbMetaData.supportsExpressionsInOrderBy()+"] => Retrieves whether this database supports expressions in ORDER BY lists."+endDelimitor;           
	        addStr += "> supportsExtendedSQLGrammar() : ["+dbMetaData.supportsExtendedSQLGrammar()+"] => Retrieves whether this database supports the ODBC Extended SQL grammar."+endDelimitor;           
	        addStr += "> supportsFullOuterJoins() : ["+dbMetaData.supportsFullOuterJoins()+"] => Retrieves whether this database supports full nested outer joins."+endDelimitor;           
	        //addStr += "> supportsGetGeneratedKeys() : ["+dbMetaData.supportsGetGeneratedKeys()+"] => "+endDelimitor;           
	        addStr += "> supportsGroupBy() : ["+dbMetaData.supportsGroupBy()+"] => Retrieves whether this database supports some form of GROUP BY clause."+endDelimitor;           
	        addStr += "> supportsGroupByBeyondSelect() : ["+dbMetaData.supportsGroupByBeyondSelect()+"] => Retrieves whether this database supports using columns not included in the SELECT statement in a GROUP BY clause provided that all of the columns in the SELECT statement are included in the GROUP BY clause."+endDelimitor;           
	        addStr += "> supportsGroupByUnrelated() : ["+dbMetaData.supportsGroupByUnrelated()+"] => Retrieves whether this database supports using a column that is not in the SELECT statement in a GROUP BY clause."+endDelimitor;          
	        addStr += "> supportsLikeEscapeClause() : ["+dbMetaData.supportsLikeEscapeClause()+"] => Retrieves whether this database supports specifying a LIKE escape clause."+endDelimitor;           
	        addStr += "> supportsLimitedOuterJoins() : ["+dbMetaData.supportsLimitedOuterJoins()+"] => Retrieves whether this database provides limited support for outer joins."+endDelimitor;           
	        addStr += "> supportsMinimumSQLGrammar() : ["+dbMetaData.supportsMinimumSQLGrammar()+"] => Retrieves whether this database supports the ODBC Minimum SQL grammar."+endDelimitor;           
	        addStr += "> supportsMixedCaseIdentifiers() : ["+dbMetaData.supportsMixedCaseIdentifiers()+"] => Retrieves whether this database treats mixed case unquoted SQL identifiers as case sensitive and as a result stores them in mixed case."+endDelimitor;           
	        addStr += "> supportsMixedCaseQuotedIdentifiers() : ["+dbMetaData.supportsMixedCaseQuotedIdentifiers()+"] => Retrieves whether this database treats mixed case quoted SQL identifiers as case sensitive and as a result stores them in mixed case."+endDelimitor;           
	        //addStr += "> supportsMultipleOpenResults() : ["+dbMetaData.supportsMultipleOpenResults()+"] => "+endDelimitor;           
	        addStr += "> supportsMultipleResultSets() : ["+dbMetaData.supportsMultipleResultSets()+"] => Retrieves whether this database supports getting multiple ResultSet objects from a single call to the method execute"+endDelimitor;           
	        addStr += "> supportsMultipleTransactions() : ["+dbMetaData.supportsMultipleTransactions()+"] => Retrieves whether this database allows having multiple transactions open at once (on different connections)."+endDelimitor;           
	        //addStr += "> supportsNamedParameters() : ["+dbMetaData.supportsNamedParameters()+"] => "+endDelimitor;           
	        addStr += "> supportsNonNullableColumns() : ["+dbMetaData.supportsNonNullableColumns()+"] => Retrieves whether columns in this database may be defined as non-nullable."+endDelimitor;           
	        addStr += "> supportsOpenCursorsAcrossCommit() : ["+dbMetaData.supportsOpenCursorsAcrossCommit()+"] => Retrieves whether this database supports keeping cursors open across commits."+endDelimitor;           
	        addStr += "> supportsOpenCursorsAcrossRollback() : ["+dbMetaData.supportsOpenCursorsAcrossRollback()+"] => Retrieves whether this database supports keeping cursors open across rollbacks."+endDelimitor;           
	        addStr += "> supportsOpenStatementsAcrossCommit() : ["+dbMetaData.supportsOpenStatementsAcrossCommit()+"] => Retrieves whether this database supports keeping statements open across commits."+endDelimitor;           
	        addStr += "> supportsOpenStatementsAcrossRollback() : ["+dbMetaData.supportsOpenStatementsAcrossRollback()+"] => Retrieves whether this database supports keeping statements open across rollbacks."+endDelimitor;           
	        addStr += "> supportsOrderByUnrelated() : ["+dbMetaData.supportsOrderByUnrelated()+"] => Retrieves whether this database supports keeping statements open across rollbacks."+endDelimitor;           
	        addStr += "> supportsOuterJoins() : ["+dbMetaData.supportsOuterJoins()+"] => Retrieves whether this database supports some form of outer join."+endDelimitor;           
	        addStr += "> supportsPositionedDelete() : ["+dbMetaData.supportsPositionedDelete()+"] => Retrieves whether this database supports positioned DELETE statements."+endDelimitor;           
	        addStr += "> supportsPositionedUpdate() : ["+dbMetaData.supportsPositionedUpdate()+"] => Retrieves whether this database supports positioned UPDATE statements."+endDelimitor;           
	        addStr += "> supportsSchemasInDataManipulation() : ["+dbMetaData.supportsSchemasInDataManipulation()+"] => Retrieves whether a schema name can be used in a data manipulation statement."+endDelimitor;           
	        addStr += "> supportsSchemasInIndexDefinitions() : ["+dbMetaData.supportsSchemasInIndexDefinitions()+"] => Retrieves whether a schema name can be used in an index definition statement."+endDelimitor;           
	        addStr += "> supportsSchemasInPrivilegeDefinitions() : ["+dbMetaData.supportsSchemasInPrivilegeDefinitions()+"] => Retrieves whether a schema name can be used in a privilege definition statement."+endDelimitor;           
	        addStr += "> supportsSchemasInProcedureCalls() : ["+dbMetaData.supportsSchemasInProcedureCalls()+"] => Retrieves whether a schema name can be used in a procedure call statement."+endDelimitor;           
	        addStr += "> supportsSelectForUpdate() : ["+dbMetaData.supportsSelectForUpdate()+"] => Retrieves whether this database supports SELECT FOR UPDATE statements."+endDelimitor;           
	        //addStr += "> supportsStatementPooling() : ["+dbMetaData.supportsStatementPooling()+"] => "+endDelimitor;           
	        addStr += "> supportsStoredProcedures() : ["+dbMetaData.supportsStoredProcedures()+"] => Retrieves whether this database supports stored procedure calls that use the stored procedure escape syntax"+endDelimitor;           
	        addStr += "> supportsSubqueriesInComparisons() : ["+dbMetaData.supportsSubqueriesInComparisons()+"] => Retrieves whether this database supports subqueries in comparison expressions."+endDelimitor;           
	        addStr += "> supportsSubqueriesInExists() : ["+dbMetaData.supportsSubqueriesInExists()+"] => Retrieves whether this database supports subqueries in EXISTS expressions."+endDelimitor;           
	        addStr += "> supportsSubqueriesInIns() : ["+dbMetaData.supportsSubqueriesInIns()+"] => Retrieves whether this database supports subqueries in IN statements."+endDelimitor;           
	        addStr += "> supportsSubqueriesInQuantifieds() : ["+dbMetaData.supportsSubqueriesInQuantifieds()+"] => Retrieves whether this database supports subqueries in quantified expressions."+endDelimitor;           
	        addStr += "> supportsTableCorrelationNames() : ["+dbMetaData.supportsTableCorrelationNames()+"] => Retrieves whether this database supports table correlation names."+endDelimitor;           
	        addStr += "> supportsTransactions() : ["+dbMetaData.supportsTransactions()+"] => Retrieves whether this database supports transactions."+endDelimitor;           
	        addStr += "> supportsUnion() : ["+dbMetaData.supportsUnion()+"] => Retrieves whether this database supports SQL UNION."+endDelimitor;           
	        addStr += "> supportsUnionAll() : ["+dbMetaData.supportsUnionAll()+"] =>Retrieves whether this database supports SQL UNION ALL."+endDelimitor;           
	        addStr += "> usesLocalFilePerTable() : ["+dbMetaData.usesLocalFilePerTable()+"] => Retrieves whether this database uses a file for each table."+endDelimitor;           
	        addStr += "> usesLocalFiles() : ["+dbMetaData.usesLocalFiles()+"] => Retrieves whether this database stores tables in a local file."+endDelimitor+endDelimitor;
		} catch (SQLException e) {
			e.printStackTrace();
		}      
		return addStr;
	}
}
