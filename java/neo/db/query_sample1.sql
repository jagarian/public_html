Author:  Graeme Birchall ?Email:   Graeme_Birchall@verizon.net
Web:     http://mysite.verizon.net/Graeme_Birchall/
Title:   DB2 9.5 SQL Cookbook ?Date:    1-Dec-2008





EMP_NM        EMP_JB       SELECT   nm.id             ANSWER
+----------+  +--------+           ,nm.name           ================
|ID|NAME   |  |ID|JOB  |           ,jb.job            ID NAME    JOB
|--|-------|  |--|-----|   FROM     emp_nm nm         -- ------- -----
|10|Sanders|  |10|Sales|           ,emp_jb jb         10 Sanders Sales
|20|Pernal |  |20|Clerk|   WHERE    nm.id = jb.id     20 Pernal  Clerk
|50|Hanes  |  +--------+   ORDER BY 1;
+----------+
Figure 1, Join example






EMP_NM        EMP_JB       SELECT   nm.id             ANSWER
+----------+  +--------+           ,nm.name           ================
|ID|NAME   |  |ID|JOB  |           ,jb.job            ID NAME    JOB
|--|-------|  |--|-----|   FROM     emp_nm nm         -- ------- -----
|10|Sanders|  |10|Sales|   LEFT OUTER JOIN            10 Sanders Sales
|20|Pernal |  |20|Clerk|            emp_jb jb         20 Pernal  Clerk
|50|Hanes  |  +--------+   ON       nm.id = jb.id     50 Hanes   -
+----------+               ORDER BY nm.id;
Figure 2,Left-outer-join example






EMP_NM        EMP_JB       SELECT   *                         ANSWER
+----------+  +--------+   FROM     emp_nm nm                 ========
|ID|NAME   |  |ID|JOB  |   WHERE NOT EXISTS                   ID NAME
|--|-------|  |--|-----|           (SELECT *                  == =====
|10|Sanders|  |10|Sales|            FROM   emp_jb jb          50 Hanes
|20|Pernal |  |20|Clerk|            WHERE  nm.id = jb.id)
|50|Hanes  |  +--------+   ORDER BY id;
+----------+
Figure 3, Sub-query example






EMP_NM        EMP_JB       SELECT   *                        ANSWER
+----------+  +--------+   FROM     emp_nm                   =========
|ID|NAME   |  |ID|JOB  |   WHERE    name < 'S'               ID 2
|--|-------|  |--|-----|   UNION                             -- ------
|10|Sanders|  |10|Sales|   SELECT   *                        10 Sales
|20|Pernal |  |20|Clerk|   FROM     emp_jb                   20 Clerk
|50|Hanes  |  +--------+   ORDER BY 1,2;                     20 Pernal
+----------+                                                 50 Hanes
Figure 4, Union example






EMP_JB       SELECT   id
+--------+           ,job                                   ANSWER
|ID|JOB  |           ,ROW_NUMBER() OVER(ORDER BY job) AS R  ==========
|--|-----|   FROM     emp_jb                                ID JOB   R
|10|Sales|   ORDER BY job;                                  -- ----- -
|20|Clerk|                                                  20 Clerk 1
+--------+                                                  10 Sales 2
Figure 5, Assign row-numbers example






EMP_JB       SELECT   id                               ANSWER
+--------+           ,job                              ===============
|ID|JOB  |           ,CASE                             ID JOB   STATUS
|--|-----|               WHEN job = 'Sales'            -- ----- ------
|10|Sales|               THEN 'Fire'                   10 Sales Fire
|20|Clerk|               ELSE 'Demote'                 20 Clerk Demote
+--------+            END AS STATUS
             FROM     emp_jb;
Figure 6, Case stmt example






FAMILY          WITH temp (persn, lvl) AS                    ANSWER
+-----------+     (SELECT  parnt, 1                          =========
|PARNT|CHILD|      FROM    family                            PERSN LVL
|-----|-----|      WHERE   parnt = 'Dad'                     ----- ---
|GrDad|Dad  |      UNION ALL                                 Dad     1
|Dad  |Dghtr|      SELECT  child, Lvl + 1                    Dghtr   2
|Dghtr|GrSon|      FROM    temp,                             GrSon   3
|Dghtr|GrDtr|              family                            GrDtr   3
+-----------+      WHERE   persn = parnt)
                SELECT *
                FROM   temp;
Figure 7, Recursion example






INPUT DATA                 Recursive SQL                   ANSWER
=================          ============>                   ===========
"Some silly text"                                          TEXT  LINE#
                                                           ----- -----
                                                           Some      1
                                                           silly     2
                                                           text      3
Figure 8, Convert string to rows






INPUT DATA                 Recursive SQL             ANSWER
===========                ============>             =================
TEXT  LINE#                                          "Some silly text"
----- -----
Some      1
silly     2
text      3
Figure 9, Convert rows to string






EMP_NM         SELECT   *                                    ANSWER
+----------+   FROM     emp_nm                               =========
|ID|NAME   |   ORDER BY id DESC                              ID NAME
|--|-------|   FETCH FIRST 2 ROWS ONLY;                      -- ------
|10|Sanders|                                                 50 Hanes
|20|Pernal |                                                 20 Pernal
|50|Hanes  |
+----------+
Figure 10, Fetch first "n" rows example






EMP_NM         SELECT   *                                   ANSWER
+----------+   FROM     emp_nm                              ==========
|ID|NAME   |   WHERE    name like 'S%'                      ID NAME
|--|-------|   WITH UR;                                     -- -------
|10|Sanders|                                                10 Sanders
|20|Pernal |
|50|Hanes  |
+----------+
Figure 11, Fetch WITH UR example






EMP_NM         SELECT   AVG(id)   AS avg             ANSWER
+----------+           ,MAX(name) AS maxn            =================
|ID|NAME   |           ,COUNT(*)  AS #rows           AVG MAXN    #ROWS
|--|-------|   FROM     emp_nm;                      --- ------- -----
|10|Sanders|                                          26 Sanders     3
|20|Pernal |
|50|Hanes  |
+----------+
Figure 12, Column Functions example






SELECT   job                                                    ANSWER
        ,dept                               ==========================
        ,SUM(salary) AS sum_sal             JOB   DEPT SUM_SAL   #EMPS
        ,COUNT(*)    AS #emps               ----- ---- --------- -----
FROM     staff                              Clerk   15  84766.70     2
WHERE    dept   < 30                        Clerk   20  77757.35     2
  AND    salary < 90000                     Clerk    - 162524.05     4
  AND    job    < 'S'                       Mgr     10 243453.45     3
GROUP BY ROLLUP(job, dept)                  Mgr     15  80659.80     1
ORDER BY job                                Mgr      - 324113.25     4
        ,dept;                              -        - 486637.30     8
Figure 13, Subtotal and Grand-total example






Figure 14, Syntax Diagram Conventions






SELECT   name        -- this is a comment.
FROM     staff       -- this is another comment.
ORDER BY id;
Figure 15, SQL Comment example






--#SET DELIMITER !
SELECT name FROM staff WHERE id = 10!
--#SET DELIMITER ;
SELECT name FROM staff WHERE id = 20;
Figure 16, Set Delimiter example






CREATE TABLE employee
(empno     CHARACTER (00006)    NOT NULL
,firstnme  VARCHAR   (00012)    NOT NULL
,midinit   CHARACTER (00001)    NOT NULL
,lastname  VARCHAR   (00015)    NOT NULL
,workdept  CHARACTER (00003)
,phoneno   CHARACTER (00004)
,hiredate  DATE
,job       CHARACTER (00008)
,edlevel   SMALLINT             NOT NULL
,SEX       CHARACTER (00001)
,birthdate DATE
,salary    DECIMAL   (00009,02)
,bonus     DECIMAL   (00009,02)
,comm      DECIMAL   (00009,02)
 )     
 DATA CAPTURE NONE;
Figure 17, DB2 sample table - EMPLOYEE






CREATE VIEW employee_view AS
SELECT   a.empno, a.firstnme, a.salary, a.workdept
FROM     employee a
WHERE    a.salary >=
        (SELECT AVG(b.salary)
         FROM   employee b
         WHERE  a.workdept = b.workdept);
Figure 18, DB2 sample view - EMPLOYEE_VIEW






CREATE VIEW silly (c1, c2, c3)
AS VALUES (11, 'AAA', SMALLINT(22))
         ,(12, 'BBB', SMALLINT(33))
         ,(13, 'CCC', NULL);
Figure 19, Define a view using a VALUES clause






SELECT   c1, c2, c3                                        ANSWER
FROM     silly                                             ===========
ORDER BY c1 aSC;                                           C1  C2   C3
                                                           --  ---  --
                                                           11  AAA  22
                                                           12  BBB  33
                                                           13  CCC   -
Figure 20, SELECT from a view that has its own data






CREATE VIEW test_data AS
WITH temp1 (num1) AS
(VALUES  (1)
 UNION ALL
 SELECT  num1 + 1
 FROM    temp1
 WHERE   num1 < 10000)
SELECT *
FROM   temp1;
Figure 21, Define a view that creates data on the fly






CREATE ALIAS  employee_al1 FOR employee;
COMMIT;
       
CREATE ALIAS  employee_al2 fOR employee_al1;
COMMIT;
       
CREATE ALIAS  employee_al3 FOR employee_al2;
COMMIT;
Figure 22, Define three aliases, the latter on the earlier






CREATE NICKNAME emp FOR unixserver.production.employee;
Figure 23, Define a nickname






SELECT   *
FROM     staff TABLESAMPLE BERNOULLI(10);
Figure 24, TABLESAMPLE example






CREATE TABLE sales_record
(sales#              INTEGER             NOT NULL
                     GENERATED ALWAYS AS IDENTITY
                     (START   WITH 1
                     ,INCREMENT BY 1
                     ,NO MAXVALUE
                     ,NO CYCLE)
,sale_ts             TIMESTAMP           NOT NULL
,num_items           SMALLINT            NOT NULL
,payment_type        CHAR(2)             NOT NULL
,sale_value          DECIMAL(12,2)       NOT NULL
,sales_tax           DECIMAL(12,2)
,employee#           INTEGER             NOT NULL
,CONSTRAINT sales1   CHECK(payment_type IN ('CS','CR'))
,CONSTRAINT sales2   CHECK(sale_value    > 0)
,CONSTRAINT sales3   CHECK(num_items     > 0)
,CONSTRAINT sales4   FOREIGN KEY(employee#)
                     REFERENCES staff(id) ON DELETE RESTRICT
,PRIMARY KEY(sales#));
Figure 25, Sample table definition






CREATE TABLE default_values
(c1       CHAR        NOT NULL
,d1       DECIMAL     NOT NULL);
Figure 26, Table with default column lengths






SELECT   DECFLOAT(+1.23)      +  NaN                    AS "      NaN"
        ,DECFLOAT(-1.23)      +  NaN                    AS "      NaN"
        ,DECFLOAT(-1.23)      + -NaN                    AS "     -NaN"
        ,DECFLOAT(+infinity)  +  NaN                    AS "      NaN"
        ,DECFLOAT(+sNaN)      +  NaN                    AS "      NaN"
        ,DECFLOAT(-sNaN)      +  NaN                    AS "     -NaN"
        ,DECFLOAT(+NaN)       +  NaN                    AS "      NaN"
        ,DECFLOAT(-NaN)       +  NaN                    AS "     -NaN"
FROM     sysibm.sysdummy1;
Figure 27, NaN arithmetic usage






SELECT   DECFLOAT(1) / +infinity                        AS "  0E-6176"
        ,DECFLOAT(1) * +infinity                        AS " Infinity"
        ,DECFLOAT(1) + +infinity                        AS " Infinity"
        ,DECFLOAT(1) - +infinity                        AS "-Infinity"
        ,DECFLOAT(1) / -infinity                        AS " -0E-6176"
        ,DECFLOAT(1) * -infinity                        AS "-Infinity"
        ,DECFLOAT(1) + -infinity                        AS "-Infinity"
        ,DECFLOAT(1) - -infinity                        AS " Infinity"
FROM     sysibm.sysdummy1;
Figure 28, Infinity arithmetic usage






SELECT   DECFLOAT(+1.23)      /  0                      AS " Infinity"
        ,DECFLOAT(-1.23)      /  0                      AS "-Infinity"
        ,DECFLOAT(+1.23)      +  infinity               AS " Infinity"
        ,DECFLOAT(0)          /  0                      AS "      NaN"
        ,DECFLOAT(infinity)   +  -infinity              AS "      NaN"
        ,LOG(DECFLOAT(0))                               AS "-Infinity"
        ,LOG(DECFLOAT(-123))                            AS "      NaN"
        ,SQRT(DECFLOAT(-123))                           AS "      NaN"
FROM     sysibm.sysdummy1;
Figure 29, DECFLOAT arithmetic results






-NaN  -sNan  -infinity  -1.2  -1.20  0  1.20  1.2  infinity  sNaN  NaN
Figure 30, DECFLOAT value order






                                                                ANSWER
WITH temp1 (d1, d2) AS                                          ======
  (VALUES (DECFLOAT(+1.0), DECFLOAT(+1.00))                          1
         ,(DECFLOAT(-1.0), DECFLOAT(-1.00))                         -1
         ,(DECFLOAT(+0.0), DECFLOAT(+0.00))                          1
         ,(DECFLOAT(-0.0), DECFLOAT(-0.00))                          1
         ,(DECFLOAT(+0),   DECFLOAT(-0))                             0
  )    
SELECT   TOTALORDER(d1,d2)
FROM     temp1;
Figure 31, Equal values that may have different orders






WITH temp1 (d1) AS
  (VALUES (DECFLOAT(+0     ,16))
         ,(DECFLOAT(+0.0   ,16))
         ,(DECFLOAT(+0.00  ,16))
         ,(DECFLOAT(+0.000 ,16))
  )    
SELECT   d1
        ,HEX(d1)                      AS hex_d1
        ,NORMALIZE_DECFLOAT(d1)       AS d2
        ,HEX(NORMALIZE_DECFLOAT(d1))  AS hex_d2
FROM     temp1;
                                                                ANSWER
                            ==========================================
                            D1    HEX_D1           D2 HEX_D2
                            ----- ---------------- -- ----------------
                                0 0000000000003822  0 0000000000003822
                              0.0 0000000000003422  0 0000000000003822
                             0.00 0000000000003022  0 0000000000003822
                            0.000 0000000000002C22  0 0000000000003822
Figure 32, Remove trailing zeros






    LABELED DURATIONS          ITEM       WORKS WITH DATE/TIME
<------------------------>     FIXED     <--------------------->
SINGULAR      PLURAL           SIZE      DATE   TIME   TIMESTAMP
===========   ============     =====     ====   ====   =========
YEAR          YEARS            N         Y      -      Y
MONTH         MONTHS           N         Y      -      Y
DAY           DAYS             Y         Y      -      Y
HOUR          HOURS            Y         -      Y      Y
MINUTE        MINUTES          Y         -      Y      Y
SECOND        SECONDS          Y         -      Y      Y
MICROSECOND   MICROSECONDS     Y         -      Y      Y
Figure 33, Labeled Durations and Date/Time Types






                                                            ANSWER
                                                            ==========
SELECT   sales_date                                     <=  1995-12-31
        ,sales_date -  10   DAY    AS d1                <=  1995-12-21
        ,sales_date +  -1   MONTH  AS d2                <=  1995-11-30
        ,sales_date +  99   YEARS  AS d3                <=  2094-12-31
        ,sales_date +  55   DAYS
                    -  22   MONTHS AS d4                <=  1994-04-24
        ,sales_date + (4+6) DAYS   AS d5                <=  1996-01-10
FROM     sales
WHERE    sales_person = 'GOUNOT'
  AND    sales_date   = '1995-12-31'
Figure 34, Example, Labeled Duration usage






                                                            ANSWER
                                                            ==========
SELECT   sales_date                                     <=  1995-12-31
        ,sales_date +    2  MONTH  AS d1                <=  1996-02-29
        ,sales_date +    3  MONTHS AS d2                <=  1996-03-31
        ,sales_date +    2  MONTH
                    +    1  MONTH  AS d3                <=  1996-03-29
        ,sales_date + (2+1) MONTHS AS d4                <=  1996-03-31
FROM     sales
WHERE    sales_person = 'GOUNOT'
  AND    sales_date   = '1995-12-31';
Figure 35, Adding Months - Varying Results






DURATION-TYPE  FORMAT         NUMBER-REPRESENTS        USE-WITH-D-TYPE
=============  =============  =====================    ===============
DATE           DECIMAL(8,0)   yyyymmdd                 TIMESTAMP, DATE
TIME           DECIMAL(6,0)   hhmmss                   TIMESTAMP, TIME
TIMESTAMP      DECIMAL(20,6)  yyyymmddhhmmss.zzzzzz    TIMESTAMP
Figure 36, Date/Time Durations






SELECT   empno                    ANSWER
        ,hiredate                 ====================================
        ,birthdate                EMPNO  HIREDATE   BIRTHDATE
        ,hiredate - birthdate     ------ ---------- ---------- -------
FROM     employee                 000150 1972-02-12 1947-05-17 240826.
WHERE    workdept = 'D11'         000200 1966-03-03 1941-05-29 240905.
  AND    lastname < 'L'           000210 1979-04-11 1953-02-23 260116.
ORDER BY empno;
Figure 37, Date Duration Generation






                                                            ANSWER
                                                            ==========
SELECT   hiredate                                       <=  1972-02-12
        ,hiredate - 12345678.                           <=  0733-03-26
        ,hiredate - 1234 years
                  -   56 months
                  -   78 days                           <=  0733-03-26
FROM     employee
WHERE    empno = '000150';
Figure 38, Subtracting a Date Duration






SPECIAL REGISTER                                 UPDATE  DATA-TYPE
===============================================  ======  =============
CURRENT CLIENT_ACCTNG                            no      VARCHAR(255)
CURRENT CLIENT_APPLNAME                          no      VARCHAR(255)
CURRENT CLIENT_USERID                            no      VARCHAR(255)
CURRENT CLIENT_WRKSTNNAME                        no      VARCHAR(255)
CURRENT DATE                                     no      DATE
CURRENT DBPARTITIONNUM                           no      INTEGER
CURRENT DECFLOAT ROUNDING MODE                   no      VARCHAR(128)
CURRENT DEFAULT TRANSFORM GROUP                  yes     VARCHAR(18)
CURRENT DEGREE                                   yes     CHAR(5)
CURRENT EXPLAIN MODE                             yes     VARCHAR(254)
CURRENT EXPLAIN SNAPSHOT                         yes     CHAR(8)
CURRENT FEDERATED ASYNCHRONY                     yes     INTEGER
CURRENT IMPLICIT XMLPARSE OPTION                 yes     VARCHAR(19)
CURRENT ISOLATION                                yes     CHAR(2)
CURRENT LOCK TIMEOUT                             yes     INTEGER
CURRENT MAINTAINED TABLE TYPES FOR OPTIMIZATION  yes     VARCHAR(254)
CURRENT MDC ROLLOUT MODE                         yes     VARCHAR(9)
CURRENT OPTIMIZATION PROFILE                     yes     VARCHAR(261)
CURRENT PACKAGE PATH                             yes     VARCHAR(4096)
CURRENT PATH                                     yes     VARCHAR(2048)
CURRENT QUERY OPTIMIZATION                       yes     INTEGER
CURRENT REFRESH AGE                              yes     DECIMAL(20,6)
CURRENT SCHEMA                                   yes     VARCHAR(128)
CURRENT SERVER                                   no      VARCHAR(128)
CURRENT TIME                                     no      TIME
CURRENT TIMESTAMP                                no      TIMESTAMP
CURRENT TIMEZONE                                 no      DECIMAL(6,0)
CURRENT USER                                     no      VARCHAR(128)
SESSION_USER                                     yes     VARCHAR(128)
SYSTEM_USER                                      no      VARCHAR(128)
USER                                             yes     VARCHAR(128)
Figure 39, DB2 Special Registers






SET CURRENT ISOLATION = RR;
SET CURRENT SCHEMA    = 'ABC';                 ANSWER
                                               =======================
SELECT  CURRENT TIME       AS cur_TIME         CUR_TIME CUR_ISO CUR_ID
       ,CURRENT ISOLATION  AS cur_ISO          -------- ------- ------
       ,CURRENT SCHEMA     AS cur_ID           12:15:16 RR      ABC
FROM    sysibm.sysdummy1;
Figure 40, Using Special Registers






Figure 41, Create Distinct Type Syntax






CREATE DISTINCT TYPE JAP_YEN AS DECIMAL(15,2) WITH COMPARISONS;
DROP   DISTINCT TYPE JAP_YEN;
Figure 42, Create and drop distinct type






CREATE TABLE customer
(id                 INTEGER               NOT NULL
,fname              VARCHAR(00010)        NOT NULL WITH DEFAULT ''
,lname              VARCHAR(00015)        NOT NULL WITH DEFAULT ''
,date_of_birth      DATE
,citizenship        CHAR(03)
,usa_sales          DECIMAL(9,2)
,eur_sales          DECIMAL(9,2)
,sales_office#      SMALLINT
,last_updated       TIMESTAMP
,PRIMARY KEY(id));
Figure 43, Sample table, without distinct types






SELECT   id
        ,usa_sales + eur_sales AS tot_sales
FROM     customer;
Figure 44, Silly query, but works






CREATE DISTINCT TYPE USA_DOLLARS AS DECIMAL(9,2) WITH COMPARISONS;
CREATE DISTINCT TYPE EUR_DOLLARS AS DECIMAL(9,2) WITH COMPARISONS;
Figure 45, Create Distinct Type examples






CREATE TABLE customer
(id                 INTEGER               NOT NULL
,fname              VARCHAR(00010)        NOT NULL WITH DEFAULT ''
,lname              VARCHAR(00015)        NOT NULL WITH DEFAULT ''
,date_of_birth      DATE
,citizenship        CHAR(03)
,usa_sales          USA_DOLLARS
,eur_sales          EUR_DOLLARS
,sales_office#      SMALLINT
,last_updated       TIMESTAMP
,PRIMARY KEY(id));
Figure 46, Sample table, with distinct types






SELECT   id
        ,usa_sales + eur_sales AS tot_sales
FROM     customer;
Figure 47, Silly query, now fails






SELECT   id
        ,DECIMAL(usa_sales) +
         DECIMAL(eur_sales) AS tot_sales
FROM     customer;
Figure 48, Silly query, works again






WITH   
   get_matching_rows AS
    (  
       SELECT   id
               ,name
               ,salary          SUBSELECT
       FROM     staff
       WHERE    id  <  50
     UNION ALL                                  FULLSELECT
       SELECT   id
               ,name
               ,salary          SUBSELECT
       FROM     staff
       WHERE    id  =  100
    )  
       
SELECT   *
FROM     get_matching_rows                       COMMON TABLE
ORDER BY id                                      EXPRESSION
FETCH FIRST 10 ROWS ONLY        SUBSELECT
FOR FETCH ONLY
WITH UR;
Figure 49, Query components






Figure 50, SELECT Statement Syntax (general)






Figure 51, SELECT Statement Syntax






SELECT   deptno                                    ANSWER
        ,admrdept                                  ===================
        ,'ABC' AS abc                              DEPTNO ADMRDEPT ABC
FROM     department                                ------ -------- ---
WHERE    deptname LIKE '%ING%'                     B01    A00      ABC
ORDER BY 1;                                        D11    D01      ABC
Figure 52, Sample SELECT statement






SELECT   *                                            ANSWER (part of)
FROM     department                                   ================
WHERE    deptname LIKE '%ING%'                        DEPTNO etc...
ORDER BY 1;                                           ------ ------>>>
                                                      B01    PLANNING
                                                      D11    MANUFACTU
Figure 53, Use "*" to select all columns in table






SELECT   deptno                                ANSWER (part of)
        ,department.*                          =======================
FROM     department                            DEPTNO DEPTNO etc...
WHERE    deptname LIKE '%ING%'                 ------ ------ ------>>>
ORDER BY 1;                                    B01    B01    PLANNING
                                               D11    D11    MANUFACTU
Figure 54, Select an individual column, and all columns






SELECT   department.*                                 ANSWER (part of)
        ,department.*                                 ================
FROM     department                                   DEPTNO etc...
WHERE    deptname LIKE '%NING%'                       ------ ------>>>
ORDER BY 1;                                           B01    PLANNING
Figure 55, Select all columns twice






Figure 56, Fetch First clause Syntax






SELECT   years                                   ANSWER
        ,name                                    =====================
        ,id                                      YEARS  NAME      ID
FROM     staff                                   ------ --------- ----
FETCH FIRST 3 ROWS ONLY;                              7 Sanders     10
                                                      8 Pernal      20
                                                      5 Marenghi    30
Figure 57, FETCH FIRST without ORDER BY, gets random rows






SELECT   years                                   ANSWER
        ,name                                    =====================
        ,id                                      YEARS  NAME      ID
FROM     staff                                   ------ --------- ----
WHERE    years IS NOT NULL                           13 Graham     310
ORDER BY years DESC                                  12 Jones      260
FETCH FIRST 3 ROWS ONLY;                             10 Hanes       50
Figure 58, FETCH FIRST with ORDER BY, gets wrong answer






SELECT   years                                   ANSWER
        ,name                                    =====================
        ,id                                      YEARS  NAME      ID
FROM     staff                                   ------ --------- ----
WHERE    years IS NOT NULL                           13 Graham     310
ORDER BY years DESC                                  12 Jones      260
        ,id    DESC                                  10 Quill      290
FETCH FIRST 3 ROWS ONLY;
Figure 59, FETCH FIRST with ORDER BY, gets right answer






SELECT   a.empno                                     ANSWER
        ,a.lastname                                  =================
FROM     employee  a                                 EMPNO  LASTNAME
        ,(SELECT MAX(empno)AS empno                  ------ ----------
          FROM   employee) AS b                      000340 GOUNOT
WHERE    a.empno = b.empno;
Figure 60, Correlation Name usage example






SELECT   a.empno                                ANSWER
        ,a.lastname                             ======================
        ,b.deptno AS dept                       EMPNO  LASTNAME   DEPT
FROM     employee   a                           ------ ---------- ----
        ,department b                           000090 HENDERSON  E11
WHERE    a.workdept  = b.deptno                 000280 SCHNEIDER  E11
  AND    a.job      <> 'SALESREP'               000290 PARKER     E11
  AND    b.deptname  = 'OPERATIONS'             000300 SMITH      E11
  AND    a.sex      IN ('M','F')                000310 SETRIGHT   E11
  AND    b.location IS NULL
ORDER BY 1;
Figure 61, Correlation name usage example






SELECT   empno    AS  e_num                        ANSWER
        ,midinit  AS "m int"                       ===================
        ,phoneno  AS "..."                         E_NUM   M INT  ...
FROM     employee                                  ------  -----  ----
WHERE    empno < '000030'                          000010  I      3978
ORDER BY 1;                                        000020  L      3476
Figure 62, Renaming fields using AS






CREATE view emp2 AS
SELECT empno    AS  e_num
      ,midinit  AS "m int"
      ,phoneno  AS "..."
FROM   employee;                                   ANSWER
                                                   ===================
SELECT *                                           E_NUM   M INT  ...
FROM   emp2                                        ------  -----  ----
WHERE "..." = '3978';                              000010  I      3978
Figure 63, View field names defined using AS






SELECT   AVG(comm)            AS a1                    ANSWER
        ,SUM(comm) / COUNT(*) AS a2                    ===============
FROM     staff                                         A1       A2
WHERE    id < 100;                                     -------  ------
                                                       796.025  530.68
Figure 64, AVG of data containing null values






SELECT   COUNT(*)      AS num                                 ANSWER
        ,MAX(lastname) AS max                                 ========
FROM     employee                                             NUM  MAX
WHERE    firstnme = 'FRED';                                   ---  ---
                                                                0  -
Figure 65, Getting a NULL value from a field defined NOT NULL






SELECT   AVG(comm)            AS a1                    ANSWER
        ,SUM(comm) / COUNT(*) AS a2                    ===============
FROM     staff                                         A1       A2
WHERE    id < 100                                      -------  ------
  AND    comm IS NOT NULL;                             796.025  796.02
Figure 66, AVG of those rows that are not null






SELECT   'JOHN'          AS J1
        ,'JOHN''S'       AS J2           ANSWER
        ,'''JOHN''S'''   AS J3           =============================
        ,'"JOHN''S"'     AS J4           J1   J2     J3       J4
FROM     staff                           ---- ------ -------- --------
WHERE    id = 10;                        JOHN JOHN'S 'JOHN'S' "JOHN'S"
Figure 67, Quote usage






SELECT   id      AS "USER ID"          ANSWER
        ,dept    AS "D#"               ===============================
        ,years   AS "#Y"               USER ID D# #Y 'TXT' "quote" fld
        ,'ABC'   AS "'TXT'"            ------- -- -- ----- -----------
        ,'"'     AS """quote"" fld"         10 20  7 ABC   "
FROM     staff s                            20 20  8 ABC   "
WHERE    id < 40                            30 38  5 ABC   "
ORDER BY "USER ID";
Figure 68, Double-quote usage






Figure 69, Basic Predicate syntax, 1 of 2






SELECT    id, job, dept                                ANSWER
FROM      staff                                        ===============
WHERE     job  =  'Mgr'                                ID   JOB   DEPT
  AND NOT job  <> 'Mgr'                                ---  ----  ----
  AND NOT job  =  'Sales'                               10  Mgr     20
  AND     id   <>  100                                  30  Mgr     38
  AND     id   >=    0                                  50  Mgr     15
  AND     id   <=  150                                 140  Mgr     51
  AND NOT dept =    50
ORDER BY  id;
Figure 70, Basic Predicate examples






Figure 71, Basic Predicate syntax, 2 of 2






SELECT   id, dept, job                                     ANSWER
FROM     staff                                             ===========
WHERE    (id,dept)  = (30,28)                              ID DEPT JOB
   OR    (id,years) = (90, 7)                              -- ---- ---
   OR    (dept,job) = (38,'Mgr')                           30   38 Mgr
ORDER BY 1;
Figure 72, Basic Predicate example, multi-value check






SELECT   id, dept, job                                     ANSWER
FROM     staff                                             ===========
WHERE    (id   = 30  AND  dept  =    28)                   ID DEPT JOB
   OR    (id   = 90  AND  years =     7)                   -- ---- ---
   OR    (dept = 38  AND  job   = 'Mgr')                   30   38 Mgr
ORDER BY 1;
Figure 73, Same query as prior, using individual predicates






Figure 74, Quantified Predicate syntax






SELECT   id, job                                              ANSWER
FROM     staff                                                ========
WHERE    job  = ANY (SELECT job FROM staff)                   ID  JOB
  AND    id  <= ALL (SELECT id  FROM staff)                   --- ----
ORDER BY id;                                                   10 Mgr
Figure 75, Quantified Predicate example, two single-value sub-queries






SELECT   id, dept, job                                  ANSWER
FROM     staff                                          ==============
WHERE    (id,dept) = ANY                                ID  DEPT JOB
         (SELECT dept, id                               --- ---- -----
          FROM   staff)                                  20   20 Sales
ORDER BY 1;
Figure 76, Quantified Predicate example, multi-value sub-query






Figure 77, BETWEEN Predicate syntax






SELECT id, job                                               ANSWER
FROM   staff                                                 =========
WHERE     id     BETWEEN 10 AND 30                           ID  JOB
  AND     id NOT BETWEEN 30 AND 10                           --- -----
  AND NOT id NOT BETWEEN 10 AND 30                            10 Mgr
ORDER BY  id;                                                 20 Sales
                                                              30 Mgr
Figure 78, BETWEEN Predicate examples






Figure 79, EXISTS Predicate syntax






SELECT id, job                                               ANSWER
FROM   staff a                                               =========
WHERE  EXISTS                                                ID  JOB
      (SELECT *                                              --- -----
       FROM   staff b                                         10 Mgr
       WHERE  b.id = a.id                                     20 Sales
         AND  b.id < 50)                                      30 Mgr
ORDER BY id;                                                  40 Sales
Figure 80, EXISTS Predicate example






Figure 81, IN Predicate syntax






SELECT id, job                                               ANSWER
FROM   staff a                                               =========
WHERE  id IN (10,20,30)                                      ID  JOB
  AND  id IN (SELECT id                                      --- -----
              FROM   staff)                                   10 Mgr
  AND  id NOT IN 99                                           20 Sales
ORDER BY id;                                                  30 Mgr
Figure 82, IN Predicate examples, single values






SELECT   empno, lastname                               ANSWER
FROM     employee                                      ===============
WHERE    (empno, 'AD3113') IN                          EMPNO  LASTNAME
         (SELECT empno, projno                         ------ -------
          FROM   emp_act                               000260 JOHNSON
          WHERE  emptime > 0.5)                        000270 PEREZ
ORDER BY 1;
Figure 83, IN Predicate example, multi-value






Figure 84, LIKE Predicate syntax






SELECT id, name                                         ANSWER
FROM   staff                                            ==============
WHERE  name LIKE 'S%n'                                  ID   NAME
   OR  name LIKE '_a_a%'                                ---  ---------
   OR  name LIKE '%r_%a'                                130  Yamaguchi
ORDER BY id;                                            200  Scoutten
Figure 85, LIKE Predicate examples






LIKE STATEMENT TEXT                             WHAT VALUES MATCH
===========================                     ======================
LIKE 'AB%'                                      Finds AB, any string
LIKE 'AB%'       ESCAPE '+'                     Finds AB, any string
LIKE 'AB+%'      ESCAPE '+'                     Finds AB%
LIKE 'AB++'      ESCAPE '+'                     Finds AB+
LIKE 'AB+%%'     ESCAPE '+'                     Finds AB%, any string
LIKE 'AB++%'     ESCAPE '+'                     Finds AB+, any string
LIKE 'AB+++%'    ESCAPE '+'                     Finds AB+%
LIKE 'AB+++%%'   ESCAPE '+'                     Finds AB+%, any string
LIKE 'AB+%+%%'   ESCAPE '+'                     Finds AB%%, any string
LIKE 'AB++++'    ESCAPE '+'                     Finds AB++
LIKE 'AB+++++%'  ESCAPE '+'                     Finds AB++%
LIKE 'AB++++%'   ESCAPE '+'                     Finds AB++, any string
LIKE 'AB+%++%'   ESCAPE '+'                     Finds AB%+, any string
Figure 86, LIKE and ESCAPE examples






SELECT id                                                       ANSWER
FROM   staff                                                    ======
WHERE  id = 10                                                     ID
  AND  'ABC' LIKE 'AB%'                                            ---
  AND  'A%C' LIKE 'A/%C'  ESCAPE '/'                                10
  AND  'A_C' LIKE 'A\_C'  ESCAPE '\'
  AND  'A_$' LIKE 'A$_$$' ESCAPE '$';
Figure 87, LIKE and ESCAPE examples






Figure 88, NULL Predicate syntax






SELECT    id, comm                                           ANSWER
FROM      staff                                              =========
WHERE     id    < 100                                        ID   COMM
  AND     id   IS NOT NULL                                   ---  ----
  AND     comm IS     NULL                                    10  -
  AND NOT comm IS NOT NULL                                    30  -
ORDER BY id;                                                  50  -
Figure 89, NULL predicate examples






SELECT   id
        ,name
FROM     staff
WHERE    name LIKE '%a' || X'3B' || '%'
ORDER BY id;
Figure 90, Refer to semi-colon in SQL text






Example:       555 +    -22  /  (12 - 3) * 66                   ANSWER
                                                                ======
                   ^    ^    ^      ^    ^                         423
                  5th  2nd  3rd    1st  4th
Figure 91, Precedence rules example






SELECT               (12   - 3)       AS int1
        ,      -22 / (12   - 3)       AS int2
        ,      -22 / (12   - 3) * 66  AS int3
        ,555 + -22 / (12   - 3) * 66  AS int4
FROM     sysibm.sysdummy1;                                      ANSWER
                                                   ===================
                                                   INT1 INT2 INT3 INT4
                                                   ---- ---- ---- ----
                                                      9   -2 -132  423
Figure 92, Precedence rules, integer example






SELECT               (12.0 - 3)       AS dec1
        ,      -22 / (12.0 - 3)       AS dec2
        ,      -22 / (12.0 - 3) * 66  AS dec3
        ,555 + -22 / (12.0 - 3) * 66  AS dec4
FROM     sysibm.sysdummy1;                                      ANSWER
                                           ===========================
                                            DEC1   DEC2   DEC3   DEC4
                                           ------ ------ ------ ------
                                              9.0   -2.4 -161.3  393.6
Figure 93, Precedence rules, decimal example






SELECT   *                          ANSWER>>   COL1 COL2   TABLE1
FROM     table1                                ---- ----   +---------+
WHERE    col1  = 'C'                           A   AA      |COL1|COL2|
  AND    col1 >= 'A'                           B   BB      |----|----|
   OR    col2 >= 'AA'                          C   CC      |A   |AA  |
ORDER BY col1;                                             |B   |BB  |
                                                           |C   |CC  |
SELECT   *                          ANSWER>>   COL1 COL2   +---------+
FROM     table1                                ---- ----
WHERE   (col1  = 'C'                           A    AA
  AND    col1 >= 'A')                          B    BB
   OR    col2 >= 'AA'                          C    CC
ORDER BY col1;
       
SELECT   *                          ANSWER>>   COL1 COL2
FROM     table1                                ---- ----
WHERE    col1  = 'C'                           C    CC
  AND   (col1 >= 'A'
   OR    col2 >= 'AA')
ORDER BY col1;
Figure 94, Use of OR and parenthesis






FROM     clause
JOIN ON  clause
WHERE    clause
GROUP BY and aggregate
HAVING   clause
SELECT   list
ORDER BY clause
FETCH FIRST
Figure 95, Query Processing Sequence






Figure 96, CAST expression syntax






SELECT   id                                          ANSWER
        ,salary                                      =================
        ,CAST(salary AS INTEGER) AS sal2             ID SALARY   SAL2
FROM     staff                                       -- -------- -----
WHERE    id < 30                                     10 98357.50 98357
ORDER BY id;                                         20 78171.25 78171
Figure 97, Use CAST expression to convert Decimal to Integer






SELECT   id                                              ANSWER
        ,job                                             =============
        ,CAST(job AS CHAR(3)) AS job2                    ID JOB   JOB2
FROM     staff                                           -- ----- ----
WHERE    id < 30                                         10 Mgr   Mgr
ORDER BY id;                                             20 Sales Sal
Figure 98, Use CAST expression to truncate Char field






SELECT   id                                                    ANSWER
        ,CAST(NULL AS SMALLINT) AS junk                        =======
FROM     staff                                                 ID JUNK
WHERE    id < 30                                               -- ----
ORDER BY id;                                                   10    -
                                                               20    -
Figure 99, Use CAST expression to define SMALLINT field with null values






SELECT   stf.id                                              ANSWER
        ,emp.empno                                           =========
FROM     staff    stf                                        ID EMPNO
LEFT OUTER JOIN                                              -- ------
         employee emp                                        10 -
ON       stf.id   =  CAST(emp.empno AS INTEGER)              20 000020
AND      emp.job  = 'MANAGER'                                30 000030
WHERE    stf.id   <  60                                      40 -
ORDER BY stf.id;                                             50 000050
Figure 100, CAST expression in join






SELECT   stf.id                                              ANSWER
        ,emp.empno                                           =========
FROM     staff    stf                                        ID EMPNO
LEFT OUTER JOIN                                              -- ------
         employee emp                                        10 -
ON       stf.id   =  INTEGER(emp.empno)                      20 000020
AND      emp.job  = 'MANAGER'                                30 000030
WHERE    stf.id   <  60                                      40 -
ORDER BY stf.id;                                             50 000050
Figure 101, Function usage in join






Figure 102, VALUES expression syntax






VALUES   6                                        <= 1 row,  1 column
VALUES  (6)                                       <= 1 row,  1 column
VALUES   6, 7, 8                                  <= 1 row,  3 columns
VALUES  (6), (7), (8)                             <= 3 rows, 1 column
VALUES  (6,66), (7,77), (8,NULL)                  <= 3 rows, 2 columns
Figure 103, VALUES usage examples






PLAIN VALUES       VALUES + WITH          VALUES + SELECT       ANSWER
============       ====================   ==================    ======
                   WITH temp (c1,c2) AS   SELECT *                1  2
VALUES  (1,2)      (VALUES  (1,2)         FROM  (VALUES (1,2)    -- --
       ,(2,3)              ,(2,3)                      ,(2,3)     3  4
       ,(3,4)              ,(3,4))                     ,(3,4)     2  3
ORDER BY 2 DESC;   SELECT   *                   )temp (c1,c2)     1  2
                   FROM     temp          ORDER BY 2 DESC;
                   ORDER BY 2 DESC;
Figure 104, Logically equivalent VALUES statements






VALUES ((SELECT COUNT(*)     FROM employee)                   ANSWER
       ,(SELECT AVG(salary)  FROM staff)              ================
       ,(SELECT MAX(deptno)  FROM department))         1         2   3
FOR FETCH ONLY                                        -- --------- ---
WITH UR;                                              42  67932.78 J22
Figure 105, VALUES running selects






WITH temp1 (col1, col2) AS                                   ANSWER
(VALUES    (   0, 'AA')                                      =========
          ,(   1, 'BB')                                      COL1 COL2
          ,(   2, NULL)                                      ---- ----
)                                                               0 AA
SELECT *                                                        1 BB
FROM   temp1;                                                   2 -
Figure 106, Use VALUES to define a temporary table (1 of 4)






WITH temp1 (col1, col2) AS                                   ANSWER
(VALUES    (DECIMAL(0 ,3,1), 'AA')                           =========
          ,(DECIMAL(1 ,3,1), 'BB')                           COL1 COL2
          ,(DECIMAL(2 ,3,1), NULL)                           ---- ----
)                                                             0.0 AA
SELECT *                                                      1.0 BB
FROM   temp1;                                                 2.0 -
Figure 107, Use VALUES to define a temporary table (2 of 4)






WITH temp1 (col1, col2) AS                                   ANSWER
(VALUES    (   0, CAST('AA' AS CHAR(1)))                     =========
          ,(   1, CAST('BB' AS CHAR(1)))                     COL1 COL2
          ,(   2, CAST(NULL AS CHAR(1)))                     ---- ----
)                                                               0 A
SELECT *                                                        1 B
FROM   temp1;                                                   2 -
Figure 108, Use VALUES to define a temporary table (3 of 4)






WITH temp1 (col1, col2) AS                                   ANSWER
(VALUES    (   0, CHAR('AA',1))                              =========
          ,(   1, CHAR('BB',1))                              COL1 COL2
          ,(   2, NULL)                                      ---- ----
)                                                               0 A
SELECT *                                                        1 B
FROM   temp1;                                                   2 -
Figure 109, Use VALUES to define a temporary table (4 of 4)






WITH temp1 (col1, col2, col3) AS                            ANSWER
(VALUES    (   0, 'AA', 0.00)                               ==========
          ,(   1, 'BB', 1.11)                               COL1B COLX
          ,(   2, 'CC', 2.22)                               ----- ----
)                                                               0 0.00
,temp2 (col1b, colx) AS                                         1 2.11
(SELECT  col1                                                   2 4.22
        ,col1 + col3
 FROM    temp1
)      
SELECT *
FROM   temp2;
Figure 110, Derive one temporary table from another






CREATE VIEW silly (c1, c2, c3)
AS VALUES (11, 'AAA', SMALLINT(22))
         ,(12, 'BBB', SMALLINT(33))
         ,(13, 'CCC', NULL);
COMMIT;
Figure 111, Define a view using a VALUES clause






WITH temp1 (col1) AS                                            ANSWER
(VALUES     0                                                   ======
 UNION ALL                                                      COL1
 SELECT col1 + 1                                                ----
 FROM   temp1                                                      0
 WHERE  col1 + 1 < 100                                             1
)                                                                  2
SELECT *                                                           3
FROM   temp1;                                                    etc
Figure 112, Use VALUES defined data to seed a recursive SQL statement






SELECT   *                                                     ANSWER
FROM    (VALUES (123,'ABC')                                    ======
               ,(234,'DEF')                                    --- ---
        )AS ttt                                                234 DEF
ORDER BY 1 DESC;                                               123 ABC
Figure 113, Generate table with unnamed columns






SELECT  id                                                      ANSWER
       ,salary   AS sal                ===============================
       ,comm     AS com                ID      SAL    COM    COMBO TYP
       ,combo                          -- -------- ------ -------- ---
       ,typ                            10 98357.50      -        - COM
FROM    staff                          10 98357.50      - 98357.50 SAL
       ,TABLE(VALUES(salary,'SAL')     20 78171.25 612.45   612.45 COM
                   ,(comm,  'COM')     20 78171.25 612.45 78171.25 SAL
       )AS tab(combo,typ)              30 77506.75      -        - COM
WHERE   id < 40                        30 77506.75      - 77506.75 SAL
ORDER BY id
        ,typ;
Figure 114, Combine columns example






Figure 115, CASE expression syntax - 1st type






SELECT   Lastname                                 ANSWER
        ,sex   AS sx                              ====================
        ,CASE  sex                                LASTNAME   SX SEXX
           WHEN 'F' THEN 'FEMALE'                 ---------- -- ------
           WHEN 'M' THEN 'MALE'                   JEFFERSON  M  MALE
           ELSE NULL                              JOHN       F  FEMALE
         END AS sexx                              JOHNSON    F  FEMALE
FROM     employee                                 JONES      M  MALE
WHERE    lastname LIKE 'J%'
ORDER BY 1;
Figure 116, Use CASE (1st type) to expand a value






Figure 117, CASE expression syntax - 2nd type






SELECT   lastname                                 ANSWER
        ,sex   AS sx                              ====================
        ,CASE                                     LASTNAME   SX SEXX
           WHEN sex = 'F' THEN 'FEMALE'           ---------- -- ------
           WHEN sex = 'M' THEN 'MALE'             JEFFERSON  M  MALE
           ELSE NULL                              JOHN       F  FEMALE
         END AS sexx                              JOHNSON    F  FEMALE
FROM     employee                                 JONES      M  MALE
WHERE    lastname LIKE 'J%'
ORDER BY 1;
Figure 118, Use CASE (1st type) to expand a value






SELECT   lastname                                  ANSWER
        ,midinit AS mi                             ===================
        ,sex     AS sx                             LASTNAME   MI SX MX
        ,CASE                                      ---------- -- -- --
           WHEN midinit > SEX                      JEFFERSON  J  M  M
           THEN midinit                            JOHN       K  K  K
           ELSE sex                                JOHNSON    P  F  P
         END AS mx                                 JONES      T  M  T
FROM     employee
WHERE    lastname LIKE 'J%'
ORDER BY 1;
Figure 119, Use CASE to display the higher of two values






SELECT   COUNT(*)                                  AS tot    ANSWER
        ,SUM(CASE sex WHEN 'F' THEN 1 ELSE 0  END) AS #f     =========
        ,SUM(CASE sex WHEN 'M' THEN 1 ELSE 0  END) AS #m     TOT #F #M
FROM     employee                                            --- -- --
WHERE    lastname LIKE 'J%';                                   4  2  2
Figure 120, Use CASE to get multiple counts in one pass






SELECT   lastname                                ANSWER
        ,LENGTH(RTRIM(lastname)) AS len          =====================
        ,SUBSTR(lastname,1,                      LASTNAME   LEN LASTNM
           CASE                                  ---------- --- ------
            WHEN LENGTH(RTRIM(lastname))         JEFFERSON    9 JEFFER
                 > 6 THEN 6                      JOHN         4 JOHN
            ELSE LENGTH(RTRIM(lastname))         JOHNSON      7 JOHNSO
           END  ) AS lastnm                      JONES        5 JONES
FROM     employee
WHERE    lastname LIKE 'J%'
ORDER BY 1;
Figure 121, Use CASE inside a function






UPDATE staff
SET    comm = CASE dept
                 WHEN 15 THEN comm * 1.1
                 WHEN 20 THEN comm * 1.2
                 WHEN 38 THEN
                    CASE
                       WHEN years  < 5 THEN comm * 1.3
                       WHEN years >= 5 THEN comm * 1.4
                       ELSE NULL
                    END
                 ELSE comm
              END
WHERE  comm IS NOT NULL
  AND  dept  < 50;
Figure 122, UPDATE statement with nested CASE expressions






WITH temp1 (c1,c2) AS                                         ANSWER
(VALUES    (88,9),(44,3),(22,0),(0,1))                        ========
SELECT c1                                                     C1 C2 C3
      ,c2                                                     -- -- --
      ,CASE c2                                                88  9  9
         WHEN 0 THEN NULL                                     44  3 14
         ELSE c1/c2                                           22  0  -
       END AS c3                                               0  1  0
FROM   temp1;
Figure 123, Use CASE to avoid divide by zero






SELECT   lastname                                    ANSWER
        ,sex                                         =================
        ,CASE                                        LASTNAME   SX SXX
           WHEN sex >= 'M' THEN 'MAL'                ---------- -- ---
           WHEN sex >= 'F' THEN 'FEM'                JEFFERSON  M  MAL
         END AS sxx                                  JOHN       F  FEM
FROM     employee                                    JOHNSON    F  FEM
WHERE    lastname LIKE 'J%'                          JONES      M  MAL
ORDER BY 1;
Figure 124, Use CASE to derive a value (correct)






SELECT   lastname                                    ANSWER
        ,sex                                         =================
        ,CASE                                        LASTNAME   SX SXX
           WHEN sex >= 'F' THEN 'FEM'                ---------- -- ---
           WHEN sex >= 'M' THEN 'MAL'                JEFFERSON  M  FEM
         END AS sxx                                  JOHN       F  FEM
FROM     employee                                    JOHNSON    F  FEM
WHERE    lastname LIKE 'J%'                          JONES      M  FEM
ORDER BY 1;
Figure 125, Use CASE to derive a value (incorrect)






SELECT   id                                    ANSWER
        ,dept                                  =======================
        ,salary                                ID  DEPT SALARY   COMM
        ,comm                                  --- ---- -------- -----
FROM     staff                                 130   42 10505.90 75.60
WHERE    CASE                                  270   66 18555.50     -
            WHEN comm      <    70  THEN 'A'   330   66 10988.00 55.50
            WHEN name   LIKE 'W%'   THEN 'B'
            WHEN salary    < 11000  THEN 'C'
            WHEN salary    < 18500
             AND dept     <>    33  THEN 'D'
            WHEN salary    < 19000  THEN 'E'
         END IN ('A','C','E')
ORDER BY id;
Figure 126, Use CASE in a predicate






                                               ANSWER
                                               =======================
                                               ID  DEPT SALARY   COMM
SELECT   id                                    --- ---- -------- -----
        ,name                                  130   42 10505.90 75.60
        ,salary                                270   66 18555.50     -
        ,comm                                  330   66 10988.00 55.50
FROM     staff
WHERE   (comm   < 70)
   OR   (salary < 11000  AND NOT  name LIKE 'W%')
   OR   (salary < 19000  AND NOT (name LIKE 'W%'
                              OR (salary < 18500 AND dept <> 33)))
ORDER BY id;
Figure 127, Same stmt as prior, without CASE predicate






Figure 128, DECLARE CURSOR statement syntax






DECLARE fred CURSOR FOR
WITH RETURN TO CALLER
SELECT   id
        ,name
        ,salary
        ,comm
FROM     staff
WHERE    id       <  :id-var
  AND    salary   >  1000
ORDER BY id ASC
FETCH FIRST  10 ROWS ONLY
OPTIMIZE FOR 10 ROWS
FOR FETCH ONLY
WITH UR
Figure 129, Sample cursor






DECLARE fred CURSOR WITH HOLD FOR
SELECT   name
        ,salary
FROM     staff
WHERE    id > :id-var
FOR UPDDATE OF salary, comm
OPEN fred
DO UNTIL SQLCODE = 100
   FETCH   fred
   INTO    :name-var
          ,:salary-var
   IF salary < 1000 THEN DO
      UPDATE  staff
      SET     salary = :new-salary-var
      WHERE CURRENT OF fred
   END-IF
END-DO 
CLOSE fred
Figure 130, Use cursor in program






SELECT  name
       ,salary
INTO    :name-var
       ,:salary-var
FROM    staff
WHERE   id = :id-var
Figure 131, Singleton select






Figure 132, PREPARE statement syntax






STATEMENT CAN BE USED BY      STATEMENT TYPE
========================      ==============
DESCRIBE                      Any statement
DECLARE CURSOR                Must be SELECT
EXECUTE                       Must not be SELECT
Figure 133, What statements can use prepared statement






Figure 134, DESCRIBE statement syntax






DESCRIBE OUTPUT SELECT * FROM staff
       
SQLDA Information
sqldaid : SQLDA     sqldabc: 896  sqln: 20  sqld: 7
Column Information
       
sqltype             sqllen  sqlname.data                sqlname.length
------------------  ------  --------------------------  --------------
500   SMALLINT           2  ID                                       2
449   VARCHAR            9  NAME                                     4
501   SMALLINT           2  DEPT                                     4
453   CHARACTER          5  JOB                                      3
501   SMALLINT           2  YEARS                                    5
485   DECIMAL         7, 2  SALARY                                   6
485   DECIMAL         7, 2  COMM                                     4
Figure 135, DESCRIBE the output columns in a select statement






DESCRIBE TABLE staff
       
Column                   Type      Type
name                     schema    name          Length   Scale  Nulls
-----------------------  --------  ------------  -------  -----  -----
ID                       SYSIBM    SMALLINT            2      0  No
NAME                     SYSIBM    VARCHAR             9      0  Yes
DEPT                     SYSIBM    SMALLINT            2      0  Yes
JOB                      SYSIBM    CHARACTER           5      0  Yes
YEARS                    SYSIBM    SMALLINT            2      0  Yes
SALARY                   SYSIBM    DECIMAL             7      2  Yes
COMM                     SYSIBM    DECIMAL             7      2  Yes
Figure 136, DESCRIBE the columns in a table






SET :host-var = CURRENT TIMESTAMP
Figure 137, SET single host-variable






SET :host-v1 = CURRENT TIME
   ,:host-v2 = CURRENT DEGREE
   ,:host-v3 = NULL
Figure 138, SET multiple host-variables






SET    (:hv1
       ,:hv2
       ,:hv3) =
(SELECT  id
        ,name
        ,salary
 FROM    staff
 WHERE   id = :id-var)
Figure 139, SET using row-fullselect






SET CONNECTION
SET CURRENT DEFAULT TRANSFORM GROUP
SET CURRENT DEGREE
SET CURRENT EXPLAIN MODE
SET CURRENT EXPLAIN SNAPSHOT
SET CURRENT ISOLATION
SET CURRENT LOCK TIMEOUT
SET CURRENT MAINTAINED TABLE TYPES FOR OPTIMIZATION
SET CURRENT PACKAGE PATH
SET CURRENT PACKAGESET
SET CURRENT QUERY OPTIMIZATION
SET CURRENT REFRESH AGE
SET ENCRYPTION PASSWORD
SET EVENT MONITOR STATE
SET INTEGRITY
SET PASSTHRU
SET PATH
SET SCHEMA
SET SERVER OPTION
SET SESSION AUTHORIZATION
Figure 140, Other SET statements






Figure 141, SAVEPOINT statement syntax






Figure 142, Example of savepoint usage






Figure 143, RELEASE SAVEPOINT statement syntax






Figure 144, ROLLBACK statement syntax






CREATE TABLE emp_act_copy
(empno              CHARACTER  (00006)    NOT NULL
,projno             CHARACTER  (00006)    NOT NULL
,actno              SMALLINT              NOT NULL
,emptime            DECIMAL    (05,02)
,emstdate           DATE
,emendate           DATE);
Figure 145, EMP_ACT_COPY sample table - DDL






Figure 146, INSERT statement syntax






INSERT INTO emp_act_copy VALUES
   ('100000' ,'ABC' ,10 ,1.4 ,'2003-10-22', '2003-11-24');
Figure 147, Single row insert






INSERT INTO emp_act_copy VALUES
   ('200000' ,'ABC' ,10 ,1.4 ,'2003-10-22', '2003-11-24')
  ,('200000' ,'DEF' ,10 ,1.4 ,'2003-10-22', '2003-11-24')
  ,('200000' ,'IJK' ,10 ,1.4 ,'2003-10-22', '2003-11-24');
Figure 148, Multi row insert






INSERT INTO emp_act_copy VALUES
   ('400000' ,'ABC' ,10 ,NULL ,DEFAULT, CURRENT DATE);
Figure 149,Using null and default values






INSERT INTO emp_act_copy (projno, emendate, actno, empno) VALUES
   ('ABC' ,DATE(CURRENT TIMESTAMP) ,123 ,'500000');
Figure 150, Explicitly listing columns being populated during insert






INSERT INTO
   (SELECT *
    FROM   emp_act_copy
    WHERE  empno < '1'
   )   
VALUES ('510000' ,'ABC' ,10 ,1.4 ,'2003-10-22', '2003-11-24');
Figure 151, Insert into a fullselect






INSERT INTO emp_act_copy
SELECT LTRIM(CHAR(id + 600000))
      ,SUBSTR(UCASE(name),1,6)
      ,salary / 229
      ,123
      ,CURRENT DATE
      ,'2003-11-11'
FROM   staff
WHERE  id < 50;
Figure 152,Insert result of select statement






INSERT INTO emp_act_copy (empno, actno, projno)
SELECT LTRIM(CHAR(id + 700000))
      ,MINUTE(CURRENT TIME)
      ,'DEF'
FROM   staff
WHERE  id < 40;
Figure 153, Insert result of select - specified columns only






INSERT   INTO emp_act_copy
SELECT * FROM emp_act_copy;
Figure 154, Stupid - insert - doubles rows






INSERT INTO emp_act_copy (empno, actno, projno)
SELECT LTRIM(CHAR(id + 800000))
      ,77
      ,'XYZ'
FROM   staff
WHERE  id < 40
UNION  
SELECT LTRIM(CHAR(id + 900000))
      ,SALARY / 100
      ,'DEF'
FROM   staff
WHERE  id < 50;
Figure 155, Inserting result of union






INSERT INTO emp_act_copy (empno, actno, projno, emptime)
WITH temp1 (col1) AS
(VALUES (1),(2),(3),(4),(5),(6))
SELECT LTRIM(CHAR(col1 + 910000))
      ,col1
      ,CHAR(col1)
      ,col1 / 2
FROM   temp1;
Figure 156, Insert from common table expression






INSERT INTO emp_act_copy (empno, actno, projno)
SELECT LTRIM(CHAR(id + 920000))
      ,id
      ,'ABC'
FROM   staff
WHERE  id < 40
  AND  NOT EXISTS
      (SELECT *
       FROM   emp_act_copy
       WHERE  empno LIKE '92%');
Figure 157, Insert with irrelevant sub-query






CREATE TABLE us_customer                  CREATE TABLE intl_customer
(cust#    INTEGER   NOT NULL              (cust#    INTEGER   NOT NULL
,cname    CHAR(10)  NOT NULL              ,cname    CHAR(10)  NOT NULL
,country  CHAR(03)  NOT NULL              ,country  CHAR(03)  NOT NULL
,CHECK    (country = 'USA')               ,CHECK    (country <> 'USA')
,PRIMARY KEY (cust#));                    ,PRIMARY KEY (cust#));
Figure 158, Customer tables - for insert usage






INSERT INTO
  (SELECT   *
   FROM     us_customer
   UNION ALL
   SELECT   *
   FROM     intl_customer)
VALUES (111,'Fred','USA')
      ,(222,'Dave','USA')
      ,(333,'Juan','MEX');
Figure 159, Insert into multiple tables






UPDATE  emp_act_copy
SET     emptime  =  NULL
       ,emendate =  DEFAULT
       ,emstdate =  CURRENT DATE + 2 DAYS
       ,actno    =  ACTNO / 2
       ,projno   =  'ABC'
WHERE   empno    = '100000';
Figure 160, Single row update






Figure 161, UPDATE statement syntax






UPDATE  emp_act_copy
SET     actno = actno / 2;
Figure 162, Mass update






UPDATE  emp_act_copy ac1
SET     actno     =  actno * 2
       ,emptime   =  actno * 2
WHERE   empno LIKE '910%';
Figure 163, Two columns get same value






UPDATE  emp_act_copy
SET     actno    = (SELECT MAX(salary) / 10
                    FROM   staff)
WHERE   empno    = '200000';
Figure 164, Update using select






UPDATE  emp_act_copy
SET    (actno
       ,emstdate
       ,projno)  = (SELECT MAX(salary)  / 10
                          ,CURRENT DATE + 2 DAYS
                          ,MIN(CHAR(id))
                    FROM   staff
                    WHERE  id <> 33)
WHERE   empno LIKE '600%';
Figure 165, Multi-row update using select






UPDATE  emp_act_copy ac1
SET    (actno
       ,emptime)  = (SELECT ac2.actno   + 1
                           ,ac1.emptime / 2
                     FROM   emp_act_copy ac2
                     WHERE  ac2.empno        LIKE '60%'
                       AND  SUBSTR(ac2.empno,3) = SUBSTR(ac1.empno,3))
WHERE   EMPNO LIKE '700%';
Figure 166, Multi-row update using correlated select






UPDATE  emp_act_copy
SET     emptime =  10
WHERE   empno   = '000010'
  AND   projno  = 'MA2100';
Figure 167, Direct update of table






UPDATE 
  (SELECT  *
   FROM    emp_act_copy
   WHERE   empno   = '000010'
     AND   projno  = 'MA2100'
  )AS ea
SET emptime = 20;
Figure 168, Update of fullselect






UPDATE 
  (SELECT   *
   FROM     staff
   ORDER BY salary DESC
   FETCH FIRST 5 ROWS ONLY
  )AS xxx
SET comm = 10000;
Figure 169, Update first "n" rows






UPDATE  emp_act_copy ea1
SET     emptime = (SELECT MAX(emptime)
                   FROM   emp_act_copy ea2
                   WHERE  ea1.empno = ea2.empno)
WHERE   empno   = '000010'
  AND   projno  = 'MA2100';
Figure 170, Set employee-time in row to MAX - for given employee






UPDATE 
  (SELECT  ea1.*
          ,MAX(emptime) OVER(PARTITION BY empno) AS maxtime
   FROM    emp_act_copy ea1
  )AS ea2
SET     emptime = maxtime
WHERE   empno   = '000010'
  AND   projno  = 'MA2100';
Figure 171, Use OLAP function to get max-time, then apply (correct)






UPDATE  emp_act_copy
SET     emptime =  MAX(emptime) OVER(PARTITION BY empno)
WHERE   empno   = '000010'
  AND   projno  = 'MA2100';
Figure 172, Use OLAP function to get max-time, then apply (wrong)






UPDATE  emp_act_copy ac1
SET    (actno
       ,emptime)  = (SELECT ROW_NUMBER() OVER()
                           ,ac1.emptime / 2
                     FROM   emp_act_copy ac2
                     WHERE  ac2.empno        LIKE '60%'
                       AND  SUBSTR(ac2.empno,3) = SUBSTR(ac1.empno,3))
WHERE   EMPNO LIKE '800%';
Figure 173, Update with correlated query






UPDATE  emp_act_copy ac1
SET    (actno
       ,emptime)  = (SELECT c1
                           ,c2
                     FROM  (SELECT ROW_NUMBER() OVER() AS c1
                                  ,actno / 100         AS c2
                                  ,empno
                            FROM   emp_act_copy
                            WHERE  empno LIKE '60%'
                    )AS ac2
                     WHERE  SUBSTR(ac2.empno,3) = SUBSTR(ac1.empno,3))
WHERE   empno LIKE '900%';
Figure 174, Update with uncorrelated query






DELETE 
FROM    emp_act_copy
WHERE   empno    = '000010'
  AND   projno   = 'MA2100'
  AND   actno    =  10;
Figure 175, Single-row delete






Figure 176, DELETE statement syntax






DELETE 
FROM    emp_act_copy;
Figure 177, Mass delete






DELETE 
FROM    emp_act_copy
WHERE   empno   LIKE '00%'
  AND   projno    >= 'MA';
Figure 178, Selective delete






DELETE 
FROM    staff s1
WHERE   id NOT IN
       (SELECT MAX(id)
        FROM   staff s2
        WHERE  s1.dept = s2.dept);
Figure 179, Correlated delete (1 of 2)






DELETE 
FROM    staff s1
WHERE   EXISTS
       (SELECT *
        FROM   staff s2
        WHERE  s2.dept = s1.dept
          AND  s2.id   > s1.id);
Figure 180, Correlated delete (2 of 2)






DELETE FROM
  (SELECT  id
          ,MAX(id) OVER(PARTITION BY dept) AS max_id
   FROM    staff
  )AS ss
WHERE id <> max_id;
Figure 181, Delete using fullselect and OLAP function






DELETE 
FROM    (SELECT   *
         FROM     staff
         ORDER BY salary DESC
         FETCH FIRST 5 ROWS ONLY
        )AS xxx;
Figure 182, Delete first "n" rows






Figure 183, Select DML statement syntax






                                                        ANSWER
                                                        ==============
SELECT   empno                                          EMPNO  PRJ ACT
        ,projno AS prj                                  ------ --- ---
        ,actno  AS act                                  200000 ABC  10
FROM     FINAL TABLE                                    200000 DEF  10
   (INSERT INTO emp_act_copy
    VALUES ('200000','ABC',10 ,1,'2003-10-22','2003-11-24')
          ,('200000','DEF',10 ,1,'2003-10-22','2003-11-24'))
ORDER BY 1,2,3;
Figure 184, Select rows inserted






SELECT   empno                                       ANSWER
        ,projno AS prj                               =================
        ,actno  AS act                               EMPNO  PRJ ACT R#
        ,row#   AS r#                                ------ --- --- --
FROM     FINAL TABLE                                 300000 ZZZ 999  1
   (INSERT INTO emp_act_copy (empno, projno, actno)  300000 VVV 111  2
    INCLUDE (row# SMALLINT)
    VALUES ('300000','ZZZ',999,1)
          ,('300000','VVV',111,2))
ORDER BY row#;
Figure 185, Include column to get insert sequence






SELECT   empno                                       ANSWER
        ,projno AS prj                               =================
        ,actno  AS act                               EMPNO  PRJ ACT R#
        ,ROW_NUMBER() OVER() AS r#                   ------ --- --- --
FROM     FINAL TABLE                                 400000 ZZZ 999  1
   (INSERT INTO emp_act_copy (empno, projno, actno)  400000 VVV 111  2
    VALUES ('400000','ZZZ',999)
          ,('400000','VVV',111))
ORDER BY INPUT SEQUENCE;
Figure 186, Select rows in insert order






SELECT   empno                                       ANSWER
        ,projno AS prj                               =================
        ,actno  AS act                               EMPNO  PRJ ACT R#
        ,ROW_NUMBER() OVER() AS r#                   ------ ---  -- --
FROM     NEW TABLE                                   600010 1    59  1
   (INSERT INTO emp_act_copy (empno, actno, projno)  600020 563  59  2
    SELECT  LTRIM(CHAR(id + 600000))                 600030 193  59  3
           ,SECOND(CURRENT TIME)
           ,CHAR(SMALLINT(RAND(1) * 1000))
    FROM    staff
    WHERE   id < 40)
ORDER BY INPUT SEQUENCE;
Figure 187, Select from an insert that has unknown values






SELECT   empno                                        ANSWER
        ,projno  AS prj                               ================
        ,emptime AS etime                             EMPNO  PRJ ETIME
FROM     OLD TABLE                                    ------ --- -----
   (UPDATE emp_act_copy                               200000 ABC  1.00
    SET    emptime = emptime * 2                      200000 DEF  1.00
    WHERE  empno   = '200000')
ORDER BY projno;
Figure 188, Select values - from before update






SELECT   projno  AS prj                                ANSWER
        ,old_t   AS old_t                              ===============
        ,emptime AS new_t                              PRJ OLD_T NEW_T
FROM     NEW TABLE                                     --- ----- -----
   (UPDATE  emp_act_copy                               ABC  2.00  0.02
    INCLUDE (old_t DECIMAL(5,2))                       DEF  2.00 11.27
    SET     emptime = emptime * RAND(1) * 10
           ,old_t   = emptime
    WHERE   empno   = '200000')
ORDER BY 1;
Figure 189, Select values - before and after update






SELECT   projno AS prj                                         ANSWER
        ,actno  AS act                                         =======
FROM     OLD TABLE                                             PRJ ACT
   (DELETE                                                     --- ---
    FROM   emp_act_copy                                        VVV 111
    WHERE  empno = '300000')                                   ZZZ 999
ORDER BY 1,2;
Figure 190, List deleted rows






SELECT   empno                                    ANSWER
        ,projno                                   ====================
        ,actno  AS act                            EMPNO  PROJNO ACT R#
        ,row#   AS r#                             ------ ------ --- --
FROM     OLD TABLE                                000260 AD3113  70  2
   (DELETE                                        000260 AD3113  80  4
    FROM    emp_act_copy                          000260 AD3113 180  6
    INCLUDE (row# SMALLINT)
    SET     row#  = ROW_NUMBER() OVER()
    WHERE   empno = '000260')
WHERE    row# = row# / 2 * 2
ORDER BY 1,2,3;
Figure 191, Assign row numbers to deleted rows






SELECT   empno                              ANSWER
        ,(SELECT  lastname                  ==========================
          FROM   (SELECT  empno AS e#       EMPNO  LASTNAME PROJNO ACT
                         ,lastname          ------ -------- ------ ---
                  FROM    employee          000010 HAAS     AD3100  10
                 )AS xxx                    000010 HAAS     MA2100  10
          WHERE   empno = e#)               000010 HAAS     MA2110  10
        ,projno AS projno                   000020 THOMPSON PL2100  30
        ,actno  AS act                      000030 KWAN     IF1000  10
FROM     OLD TABLE
   (DELETE
    FROM    emp_act_copy
    WHERE   empno < '0001')
ORDER BY 1,2,3
FETCH FIRST 5 ROWS ONLY;
Figure 192, Join result to another table






Figure 193, MERGE statement syntax






CREATE TABLE old_staff AS            OLD_STAFF            NEW_STAFF
   (SELECT id, job, salary           +-----------------+  +----------+
    FROM   staff)                    |ID|JOB  |SALARY  |  |ID|SALARY |
WITH NO DATA;                        |--|-----|--------|  |--|-------|
                                     |20|Sales|78171.25|  |30|7750.67|
CREATE TABLE new_staff AS            |30|Mgr  |77506.75|  |40|7800.60|
   (SELECT id, salary                |40|Sales|78006.00|  |50|8065.98|
    FROM   staff)                    +-----------------+  +----------+
WITH NO DATA;
       
INSERT INTO old_staff                INSERT INTO new_staff
SELECT id, job, salary               SELECT id, salary / 10
FROM   staff                         FROM   staff
WHERE  id BETWEEN 20 and 40;         WHERE  id BETWEEN 30 and 50;
Figure 194, Sample tables for merge






MERGE INTO old_staff oo              OLD_STAFF            NEW_STAFF
USING new_staff nn                   +-----------------+  +----------+
ON    oo.id = nn.id                  |ID|JOB  |SALARY  |  |ID|SALARY |
WHEN MATCHED THEN                    |--|-----|--------|  |--|-------|
   UPDATE                            |20|Sales|78171.25|  |30|7750.67|
   SET oo.salary = nn.salary         |30|Mgr  |77506.75|  |40|7800.60|
WHEN NOT MATCHED THEN                |40|Sales|78006.00|  |50|8065.98|
   INSERT                            +-----------------+  +----------+
   VALUES (nn.id,'?',nn.salary);
                                                     AFTER-MERGE
                                                     =================
                                                     ID JOB   SALARY
                                                     -- ----- --------
                                                     20 Sales 78171.25
                                                     30 Mgr    7750.67
                                                     40 Sales  7800.60
                                                     50 ?      8065.98
Figure 195, Merge - do update or insert






MERGE INTO old_staff oo                              AFTER-MERGE
USING new_staff nn                                   =================
ON    oo.id = nn.id                                  ID JOB   SALARY
WHEN MATCHED THEN                                    -- ----- --------
   DELETE;                                           20 Sales 78171.25
Figure 196, Merge - delete if match






MERGE INTO old_staff oo              OLD_STAFF            NEW_STAFF
USING new_staff nn                   +-----------------+  +----------+
ON    oo.id = nn.id                  |ID|JOB  |SALARY  |  |ID|SALARY |
WHEN MATCHED                         |--|-----|--------|  |--|-------|
AND  oo.salary < 78000 THEN          |20|Sales|78171.25|  |30|7750.67|
   UPDATE                            |30|Mgr  |77506.75|  |40|7800.60|
   SET oo.salary = nn.salary         |40|Sales|78006.00|  |50|8065.98|
WHEN MATCHED                         +-----------------+  +----------+
AND  oo.salary > 78000 THEN
   DELETE                                            AFTER-MERGE
WHEN NOT MATCHED                                     =================
AND  nn.id > 10 THEN                                 ID JOB   SALARY
   INSERT                                            -- ----- --------
   VALUES (nn.id,'?',nn.salary)                      20 Sales 78171.25
WHEN NOT MATCHED THEN                                30 Mgr    7750.67
   SIGNAL SQLSTATE '70001'                           50 ?      8065.98
   SET MESSAGE_TEXT = 'New ID <= 10';
Figure 197, Merge with multiple options






MERGE INTO old_staff                                 AFTER-MERGE
USING                                                =================
  (SELECT MAX(id) + 1 AS max_id                      ID JOB   SALARY
         ,MAX(job)    AS max_job                     -- ----- --------
         ,MAX(salary) AS max_sal                     20 Sales 78171.25
   FROM   old_staff                                  30 Mgr   77506.75
  )AS mx                                             40 Sales 78006.00
ON    id = max_id                                    41 Sales 78171.25
WHEN NOT MATCHED THEN
   INSERT
   VALUES (max_id, max_job, max_sal);
Figure 198, Merge MAX row into table






INSERT INTO old_staff
SELECT MAX(id) + 1 AS max_id
      ,MAX(job)    AS max_job
      ,MAX(salary) AS max_sal
FROM   old_staff;
Figure 199, Merge logic - done using insert






MERGE INTO                           OLD_STAFF            NEW_STAFF
  (SELECT *                          +-----------------+  +----------+
   FROM   old_staff                  |ID|JOB  |SALARY  |  |ID|SALARY |
   WHERE  id < 40                    |--|-----|--------|  |--|-------|
  )AS oo                             |20|Sales|78171.25|  |30|7750.67|
USING                                |30|Mgr  |77506.75|  |40|7800.60|
  (SELECT *                          |40|Sales|78006.00|  |50|8065.98|
   FROM   new_staff                  +-----------------+  +----------+
   WHERE  id < 50
  )AS nn                                             AFTER-MERGE
ON    oo.id = nn.id                                  =================
WHEN MATCHED THEN                                    ID JOB   SALARY
   DELETE                                            -- ----- --------
WHEN NOT MATCHED THEN                                20 Sales 78171.25
   INSERT                                            40 ?      7800.60
   VALUES (nn.id,'?',nn.salary);                     40 Sales 78006.00
Figure 200, Merge using two fullselects






MERGE INTO old_staff oo                              AFTER-MERGE
USING new_staff nn                                   =================
ON    oo.id = nn.id                                  ID JOB   SALARY
WHEN MATCHED THEN                                    -- ----- --------
   UPDATE                                            20 Sales 78171.25
   SET (salary,job) = (1234,'?')                     30 ?      1234.00
WHEN NOT MATCHED THEN                                40 ?      1234.00
   INSERT (id,salary,job)                            50 ?      5678.90
   VALUES (id,5678.9,'?');
Figure 201, Listing columns and values in insert






Figure 202, Compound SQL Statement syntax






BEGIN ATOMIC
   DECLARE cntr SMALLINT DEFAULT 1;
   FOR V1 AS
      SELECT   id as idval
      FROM     staff
      WHERE    id < 80
      ORDER BY id
   DO  
      UPDATE   staff
      SET      comm = cntr
      WHERE    id   = idval;
      SET cntr = cntr + 1;
   END FOR;
END    
Figure 203, Sample Compound SQL statement






--#SET DELIMITER !
       
SELECT NAME FROM STAFF WHERE id = 10!
       
--#SET DELIMITER ;
       
SELECT NAME FROM STAFF WHERE id = 20;
Figure 204, Set Delimiter example






BEGIN ATOMIC
   DECLARE aaa, bbb, ccc SMALLINT DEFAULT 1;
   DECLARE ddd           CHAR(10) DEFAULT NULL;
   DECLARE eee           INTEGER;
   SET eee = aaa + 1;
   UPDATE   staff
   SET      comm   = aaa
           ,salary = bbb
           ,years  = eee
   WHERE    id     = 10;
END    
Figure 205, DECLARE examples






Figure 206, FOR statement syntax






BEGIN ATOMIC                                                    BEFORE
   FOR V1 AS                                      ====================
      SELECT   years    AS yr_num                 ID     SALARY   COMM
              ,max(id)  AS max_id                 --- --------- ------
      FROM     staff                              180  37009.75 236.50
      WHERE    years < 4                          230  83369.80 189.65
      GROUP BY years                              330  49988.00  55.50
      ORDER BY years
   DO  
      UPDATE staff                                               AFTER
      SET    salary = salary / 10                 ====================
      WHERE  id     = max_id;                     ID     SALARY   COMM
      UPDATE staff                                --- --------- ------
      set    comm   = 0                           180  37009.75   0.00
      WHERE  years  = yr_num;                     230   8336.98   0.00
   END FOR;                                       330   4998.80   0.00
END    
Figure 207, FOR statement example






Figure 208, GET DIAGNOSTICS statement syntax






BEGIN ATOMIC
   DECLARE numrows INT DEFAULT 0;
   UPDATE staff
   SET    salary = 12345
   WHERE  id < 100;
   GET DIAGNOSTICS numrows = ROW_COUNT;
   UPDATE staff
   SET    salary = numrows
   WHERE  id = 10;
END    
Figure 209, GET DIAGNOSTICS statement example






Figure 210, IF statement syntax






BEGIN ATOMIC
   DECLARE cur INT;
   SET cur = MICROSECOND(CURRENT TIMESTAMP);
   IF cur > 600000 THEN
      UPDATE staff
      SET    name = CHAR(cur)
      WHERE  id   = 10;
   ELSEIF cur > 300000 THEN
      UPDATE staff
      SET    name = CHAR(cur)
      WHERE  id   = 20;
   ELSE
      UPDATE staff
      SET    name = CHAR(cur)
      WHERE  id   = 30;
   END IF;
END    
Figure 211, IF statement example






Figure 212, ITERATE statement syntax






BEGIN ATOMIC
   DECLARE cntr INT DEFAULT 0;
   whileloop:
   WHILE cntr < 60  DO
      SET cntr = cntr + 10;
      UPDATE staff
      SET    salary = cntr
      WHERE  id     = cntr;
      ITERATE whileloop;
      UPDATE staff
      SET    comm   = cntr + 1
      WHERE  id     = cntr;
   END WHILE;
END    
Figure 213, ITERATE statement example






Figure 214, LEAVE statement syntax






BEGIN ATOMIC
   DECLARE cntr INT DEFAULT 1;
   whileloop:
   WHILE 1 <> 2 DO
      SET cntr = cntr + 1;
      IF RAND() > 0.99 THEN
         LEAVE whileloop;
      END IF;
   END WHILE;
   UPDATE staff
   SET    salary = cntr
   WHERE  id = 10;
END    
Figure 215, LEAVE statement example






Figure 216, SIGNAL statement syntax






BEGIN ATOMIC
   DECLARE cntr INT DEFAULT 1;
   DECLARE emsg CHAR(20);
   whileloop:
   WHILE RAND() < .99 DO
      SET cntr = cntr + 1;
   END WHILE;
   SET emsg = '#loops: ' || CHAR(cntr);
   SIGNAL SQLSTATE '75001' SET MESSAGE_TEXT = emsg;
END    
Figure 217, SIGNAL statement example






Figure 218, WHILE statement syntax






BEGIN ATOMIC
   DECLARE c1, C2 INT DEFAULT 1;
   WHILE c1 < 10 DO
      WHILE c2 < 20 DO
         SET c2 = c2 + 1;
      END WHILE;
      SET c1 = c1 + 1;
   END WHILE;
   UPDATE staff
   SET    salary = c1
         ,comm   = c2
   WHERE  id     = 10;
END    
Figure 219, WHILE statement example






SELECT   dept                                               ANSWER
        ,count(*) as #rows                                  ==========
FROM     staff                                              DEPT #ROWS
GROUP BY dept                                               ---- -----
ORDER BY dept;                                                10     4
                                                              15     4
                                                              20     4
                                                              38     5
                                                              42     4
                                                              51     5
                                                              66     5
                                                              84     4
Figure 220, List departments in STAFF table






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE TABLE dpt                                          This example
(dept    SMALLINT     NOT NULL                            uses an "!"
,#names  SMALLINT                                         as the stmt
,PRIMARY KEY(dept))!                                      delimiter.
COMMIT!
       
CREATE TRIGGER dpt1 AFTER INSERT ON dpt
REFERENCING NEW AS NNN
FOR EACH ROW
MODE DB2SQL
BEGIN ATOMIC
   DECLARE namecnt SMALLINT DEFAULT 0;
   FOR getnames AS
      SELECT   COUNT(*) AS #n
      FROM     staff
      WHERE    dept = nnn.dept
   DO  
      SET namecnt = #n;
   END FOR;
   UPDATE dpt
   SET    #names = namecnt
   WHERE  dept   = nnn.dept;                               ANSWER
END!                                                       ===========
COMMIT!                                                    DEPT #NAMES
                                                           ---- ------
INSERT INTO dpt (dept)                                       10      4
SELECT DISTINCT dept                                         15      4
FROM   staff!                                                20      4
COMMIT!                                                      38      5
                                                             42      4
SELECT   *                                                   51      5
FROM     dpt                                                 66      5
ORDER BY dept!                                               84      4
Figure 221, Trigger with compound SQL






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE FUNCTION dpt1 (deptin SMALLINT)                    This example
RETURNS SMALLINT                                          uses an "!"
BEGIN ATOMIC                                              as the stmt
   DECLARE num_names SMALLINT;                            delimiter.
   FOR getnames AS
      SELECT   COUNT(*) AS #n
      FROM     staff
      WHERE    dept = deptin
   DO  
      SET num_names = #n;
   END FOR;                                                ANSWER
   RETURN num_names;                                       ===========
END!                                                       DEPT #NAMES
COMMIT!                                                    ---- ------
                                                             10      4
SELECT   XXX.*                                               15      4
        ,dpt1(dept) as #names                                20      4
FROM    (SELECT   dept                                       38      5
         FROM     staff                                      42      4
         GROUP BY dept                                       51      5
        )AS XXX                                              66      5
ORDER BY dept!                                               84      4
Figure 222, Scalar Function with compound SQL






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE FUNCTION dpt1 (deptin SMALLINT)                    This example
RETURNS SMALLINT                                          uses an "!"
BEGIN ATOMIC                                              as the stmt
   RETURN                                                 delimiter.
   SELECT COUNT(*)
   FROM   staff
   WHERE  dept = deptin;
END!   
COMMIT!
       
SELECT   XXX.*
        ,dpt1(dept) as #names
FROM    (SELECT   dept
         FROM     staff
         GROUP BY dept
        )AS XXX
ORDER BY dept!
Figure 223, Scalar Function with compound SQL






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE FUNCTION dpt2 ()                                   This example
RETURNS TABLE (dept    SMALLINT                           uses an "!"
              ,#names  SMALLINT)                          as the stmt
BEGIN ATOMIC                                              delimiter.
   RETURN
   SELECT   dept
           ,count(*)                                       ANSWER
   FROM     staff                                          ===========
   GROUP BY dept                                           DEPT #NAMES
   ORDER BY dept;                                          ---- ------
END!                                                         10      4
COMMIT!                                                      15      4
                                                             20      4
--#SET DELIMITER ;                                           38      5
                                                             42      4
SELECT   *                                                   51      5
FROM     TABLE(dpt2()) T1                                    66      5
ORDER BY dept;                                               84      4
Figure 224, Table Function with compound SQL






Figure 225, ARRAY_AGG function syntax






Figure 226, AVG function syntax






SELECT   AVG(dept)          AS a1                       ANSWER
        ,AVG(ALL dept)      AS a2                       ==============
        ,AVG(DISTINCT dept) AS a3                       A1 A2 A3 A4 A5
        ,AVG(dept/10)       AS a4                       -- -- -- -- --
        ,AVG(dept)/10       AS a5                       41 41 40  3  4
FROM     staff
HAVING   AVG(dept) > 40;
Figure 227, AVG function examples






UPDATE staff
SET    comm = 0
WHERE  comm IS NULL;
      
SELECT AVG(salary) AS salary                   ANSWER
      ,AVG(comm)   AS comm1                    ======================
      ,AVG(CASE comm                            SALARY   COMM1  COMM2
             WHEN 0 THEN NULL                  -------- ------ ------
             ELSE comm                         67932.78 351.98 513.31
           END) AS comm2
FROM   staff;
      
UPDATE staff
SET    comm = NULL
WHERE  comm = 0;
Figure 228, Convert zero to null before doing AVG






SELECT   COUNT(*) AS c1                                    ANSWER
        ,AVG(salary) AS a1                                 ===========
        ,COALESCE(AVG(salary),0) AS a2                     C1 A1 A2 A3
        ,CASE                                              -- -- -- --
           WHEN AVG(salary) IS NULL THEN 0                  0  -  0  0
           ELSE AVG(salary)
         END AS a3
FROM     staff
WHERE    id < 10;
Figure 229, Convert null output (from AVG) to zero






SELECT  AVG(DAYS(birthdate))                         ANSWER
       ,DATE(AVG(DAYS(birthdate)))                   =================
FROM    employee;                                    1      2
                                                     ------ ----------
                                                     721092 1975-04-14
Figure 230, AVG of date column






SELECT  AVG(avg_sal) AS avg_avg                       ANSWER
FROM   (SELECT   dept                                 ================
                ,AVG(salary) AS avg_sal               
        FROM     staff
        GROUP BY dept
       )AS xxx;
Figure 231, Select average of average






Figure 232, CORRELATION function syntax






WITH temp1(col1, col2, col3, col4) AS      ANSWER
(VALUES   (0   , 0   , 0   , RAND(1))      ===========================
 UNION ALL                                 COR11  COR12  COR23  COR34
 SELECT col1 + 1                           ------ ------ ------ ------
       ,col2 - 1                            1.000 -1.000 -0.017 -0.005
       ,RAND()
       ,RAND()
 FROM   temp1
 WHERE  col1 <= 1000
)      
SELECT DEC(CORRELATION(col1,col1),5,3)  AS cor11
      ,DEC(CORRELATION(col1,col2),5,3)  AS cor12
      ,DEC(CORRELATION(col2,col3),5,3)  AS cor23
      ,DEC(CORRELATION(col3,col4),5,3)  AS cor34
FROM   temp1;
Figure 233, CORRELATION function examples






Figure 234, COUNT function syntax






SELECT COUNT(*)                     AS c1            ANSWER
      ,COUNT(INT(comm/10))          AS c2            =================
      ,COUNT(ALL INT(comm/10))      AS c3            C1 C2 C3 C4 C5 C6
      ,COUNT(DISTINCT INT(comm/10)) AS c4            -- -- -- -- -- --
      ,COUNT(DISTINCT INT(comm))    AS c5            35 24 24 19 24  2
      ,COUNT(DISTINCT INT(comm))/10 AS c6
FROM   staff;
Figure 235, COUNT function examples






SELECT   'NO GP-BY'  AS c1                                ANSWER
        ,COUNT(*)    AS c2                                ============
FROM     staff                                            C1        C2
WHERE    id = -1                                          --------  --
UNION                                                     NO GP-BY   0
SELECT   'GROUP-BY'  AS c1
        ,COUNT(*)    AS c2
FROM     staff
WHERE    id = -1
GROUP BY dept;
Figure 236, COUNT function with and without GROUP BY






Figure 237, COUNT_BIG function syntax






SELECT   COUNT_BIG(*)                AS c1         ANSWER
        ,COUNT_BIG(dept)             AS c2         ===================
        ,COUNT_BIG(DISTINCT dept)    AS c3         C1  C2  C3  C4  C5
        ,COUNT_BIG(DISTINCT dept/10) AS c4         --- --- --- --- ---
        ,COUNT_BIG(DISTINCT dept)/10 AS c5         35. 35.  8.  7.  0.
FROM     STAFF;
Figure 238, COUNT_BIG function examples






Figure 239, COVARIANCE function syntax






WITH temp1(c1, c2, c3, c4) AS          ANSWER
(VALUES   (0 , 0 , 0 , RAND(1))        ===============================
 UNION ALL                             COV11   COV12   COV23   COV34
 SELECT c1 + 1                         ------- ------- ------- -------
       ,c2 - 1                          83666. -83666. -1.4689 -0.0004
       ,RAND()
       ,RAND()
 FROM   temp1
 WHERE  c1 <= 1000
)      
SELECT DEC(COVARIANCE(c1,c1),6,0)  AS cov11
      ,DEC(COVARIANCE(c1,c2),6,0)  AS cov12
      ,DEC(COVARIANCE(c2,c3),6,4)  AS cov23
      ,DEC(COVARIANCE(c3,c4),6,4)  AS cov34
FROM   temp1;
Figure 240, COVARIANCE function examples






Figure 241, GROUPING function syntax






SELECT   dept                                         ANSWER
        ,AVG(salary)    AS salary                     ================
        ,GROUPING(dept) AS df                         DEPT SALARY   DF
FROM     staff                                        ---- -------- --
GROUP BY ROLLUP(dept)                                   10 83365.86  0
ORDER BY dept;                                          15 60482.33  0
                                                        20 63571.52  0
                                                        38 60457.11  0
                                                        42 49592.26  0
                                                        51 83218.16  0
                                                        66 73015.24  0
                                                        84 66536.75  0
                                                         - 67932.78  1
Figure 242, GROUPING function example






Figure 243, MAX function syntax






SELECT   MAX(dept)                                     ANSWER
        ,MAX(ALL dept)                                 ===============
        ,MAX(DISTINCT dept)                             1   2   3   4
        ,MAX(DISTINCT dept/10)                         --- --- --- ---
FROM     staff;                                         84  84  84   8
Figure 244, MAX function examples






SELECT MAX(hiredate)                  ANSWER
      ,CHAR(MAX(hiredate),USA)        ================================
      ,MAX(CHAR(hiredate,USA))        1          2          3
FROM   employee;                      ---------- ---------- ----------
                                      2006-12-15 12/15/2006 12/15/2006
Figure 245, MAX function with dates






SELECT MAX(id)         AS id                       ANSWER
      ,MAX(CHAR(id))   AS chr                      ===================
      ,MAX(DIGITS(id)) AS dig                      ID     CHR    DIG
FROM   staff;                                      ------ ------ -----
                                                      350 90     00350
Figure 246, MAX function with numbers, 1 of 2






SELECT MAX(id - 250)         AS id               ANSWER
      ,MAX(CHAR(id - 250))   AS chr              =====================
      ,MAX(DIGITS(id - 250)) AS dig              ID    CHR  DIG
FROM   staff;                                    ----- ---- ----------
                                                   100 90   0000000240
Figure 247, MAX function with numbers, 2 of 2






Figure 248, MIN function syntax






SELECT   MIN(dept)                                     ANSWER
        ,MIN(ALL dept)                                 ===============
        ,MIN(DISTINCT dept)                             1   2   3   4
        ,MIN(DISTINCT dept/10)                         --- --- --- ---
FROM     staff;                                         10  10  10   1
Figure 249, MIN function examples






Figure 250, REGRESSION functions syntax






                                                            ANSWERS
                                                            ==========
SELECT  DEC(REGR_SLOPE(bonus,salary)    ,7,5)  AS r_slope      0.00247
       ,DEC(REGR_INTERCEPT(bonus,salary),7,3)  AS r_icpt       644.862
       ,INT(REGR_COUNT(bonus,salary)        )  AS r_count            5
       ,INT(REGR_AVGX(bonus,salary)         )  AS r_avgx         70850
       ,INT(REGR_AVGY(bonus,salary)         )  AS r_avgy           820
       ,DEC(REGR_SXX(bonus,salary)       ,10)  AS r_sxx     8784575000
       ,INT(REGR_SXY(bonus,salary)          )  AS r_sxy       21715000
       ,INT(REGR_SYY(bonus,salary)          )  AS r_syy         168000
FROM    employee
WHERE   workdept = 'A00';
Figure 251, REGRESSION functions examples






Figure 252, STDDEV function syntax






                                       ANSWER
                                       ===============================
                                       A1 S1            S2   S3   S4
                                       -- ------------- ---- ---- ----
SELECT AVG(dept) AS a1                 41 +2.3522355E+1 23.5 23.5 24.1
      ,STDDEV(dept) AS s1
      ,DEC(STDDEV(dept),3,1) AS s2
      ,DEC(STDDEV(ALL dept),3,1) AS s3
      ,DEC(STDDEV(DISTINCT dept),3,1) AS s4
FROM   staff;
Figure 253, STDDEV function examples






Figure 254, SUM function syntax






SELECT   SUM(dept)          AS s1             ANSWER
        ,SUM(ALL dept)      AS s2             ========================
        ,SUM(DISTINCT dept) AS s3              S1   S2   S3   S4   S5
        ,SUM(dept/10)       AS s4             ---- ---- ---- ---- ----
        ,SUM(dept)/10       AS s5             1459 1459  326  134  145
FROM     staff;
Figure 255, SUM function examples






Figure 256, VARIANCE function syntax






                                        ANSWER
                                        ==============================
                                        A1 V1              V2  V3  V4
                                        -- --------------- --- --- ---
SELECT AVG(dept) AS a1                  41 +5.533012244E+2 553 553 582
      ,VARIANCE(dept) AS s1
      ,DEC(VARIANCE(dept),4,1) AS s2
      ,DEC(VARIANCE(ALL dept),4,1) AS s3
      ,DEC(VARIANCE(DISTINCT dept),4,1) AS s4
FROM   staff;
Figure 257, VARIANCE function examples






SELECT   s1.job, s1.id, s1.salary                    ANSWER
FROM     staff s1                                    =================
WHERE    s1.name LIKE '%s%'                          JOB   ID SALARY
  AND    s1.id      <  90                            ----- -- --------
ORDER BY s1.job                                      Clerk 80 43504.60
        ,s1.id;                                      Mgr   10 98357.50
                                                     Mgr   50 80659.80
Figure 258, Select rows from STAFF table






SELECT   s1.job, s1.id, s1.salary
        ,SUM(salary)  OVER(ORDER BY job, id) AS sumsal
        ,ROW_NUMBER() OVER(ORDER BY job, id) AS r               ANSWER
FROM     staff s1                                               ======
WHERE    s1.name LIKE '%s%'              JOB   ID SALARY   SUMSAL    R
  AND    s1.id      <  90                ----- -- -------- --------- -
ORDER BY s1.job                          Clerk 80 43504.60  43504.60 1
        ,s1.id;                          Mgr   10 98357.50 141862.10 2
                                         Mgr   50 80659.80 222521.90 3
Figure 259, Using OLAP functions to get additional fields






WITH temp1 AS                                                   ANSWER
   (SELECT   *                           =============================
    FROM     staff s1                    JOB   ID SALARY   SUMSAL    R
    WHERE    s1.name LIKE '%s%'          ----- -- -------- --------- -
      AND    s1.id      <  90            Clerk 80 43504.60  43504.60 1
   )                                     Mgr   10 98357.50 141862.10 2
SELECT   s1.job, s1.id, s1.salary        Mgr   50 80659.80 222521.90 3
        ,(SELECT   SUM(s2.salary)
          FROM     temp1 s2
          WHERE   (s2.job < s1.job)
             OR   (s2.job = s1.job AND s2.id <= s1.id)) AS sumsal
        ,(SELECT   COUNT(*)
          FROM     temp1 s2
          WHERE   (s2.job < s1.job)
             OR   (s2.job = s1.job AND s2.id <= s1.id)) AS r
FROM     temp1 s1
ORDER BY s1.job
        ,s1.id;
Figure 260, Running counts without OLAP functions






Figure 261, Sample OLAP query






SELECT   dept ,id
        ,salary
        ,DEC(AVG(salary) OVER()                          ,8,2) AS avg1
        ,DEC(AVG(salary) OVER(PARTITION BY dept)         ,8,2) AS avg2
        ,DEC(AVG(salary) OVER(PARTITION BY dept
                              ORDER BY id
                              ROWS UNBOUNDED PRECEDING)  ,8,2) AS avg3
        ,DEC(AVG(salary) OVER(PARTITION BY dept
                              ORDER BY id
                              ROWS BETWEEN 1 PRECEDING
                                       AND 2 FOLLOWING)  ,8,2) AS avg4
FROM     staff
WHERE    dept  IN  (15,20)
  AND    id     >   20
ORDER BY dept ,id;
                                                                ANSWER
                 =====================================================
                 DEPT  ID   SALARY     AVG1     AVG2     AVG3     AVG4
                 ---- --- -------- -------- -------- -------- --------
                   15  50 80659.80 53281.11 60482.33 80659.80 66556.94
                   15  70 76502.83 53281.11 60482.33 78581.31 60482.33
                   15 110 42508.20 53281.11 60482.33 66556.94 53756.51
                   15 170 42258.50 53281.11 60482.33 60482.33 42383.35
                   20  80 43504.60 53281.11 38878.67 43504.60 38878.67
                   20 190 34252.75 53281.11 38878.67 38878.67 38878.67
Figure 262, Sample OLAP query






Figure 263, PARTITION BY syntax






SELECT   id  ,dept ,job  ,years  ,salary
        ,DEC(AVG(salary) OVER(PARTITION BY dept)      ,7,2) AS dpt_avg
        ,DEC(AVG(salary) OVER(PARTITION BY job)       ,7,2) AS job_avg
        ,DEC(AVG(salary) OVER(PARTITION BY years/2)   ,7,2) AS yr2_avg
        ,DEC(AVG(salary) OVER(PARTITION BY dept, job) ,7,2) AS d_j_avg
FROM     staff
WHERE    dept  IN  (15,20)
  AND    id     >   20
ORDER BY id;
                                                                ANSWER
     =================================================================
     ID  DEPT JOB   YEARS   SALARY  DPT_AVG  JOB_AVG  YR2_AVG  D_J_AVG
     --- ---- ----- ----- -------- -------- -------- -------- --------
      50   15 Mgr      10 80659.80 60482.33 80659.80 80659.80 80659.80
      70   15 Sales     7 76502.83 60482.33 76502.83 76502.83 76502.83
      80   20 Clerk     - 43504.60 38878.67 40631.01 43504.60 38878.67
     110   15 Clerk     5 42508.20 60482.33 40631.01 42383.35 42383.35
     170   15 Clerk     4 42258.50 60482.33 40631.01 42383.35 42383.35
     190   20 Clerk     8 34252.75 38878.67 40631.01 34252.75 38878.67
Figure 264, PARTITION BY examples






SELECT   dept                                         ANSWER
        ,SUM(years) AS sum                            ================
        ,AVG(years) AS avg                            DEPT SUM AVG ROW
        ,COUNT(*)   AS row                            ---- --- --- ---
FROM     staff                                          15  22   7   3
WHERE    id BETWEEN 40 AND 120                          38   6   6   1
  AND    years IS NOT NULL                              42  13   6   2
GROUP BY dept;
Figure 265, Sample query using GROUP BY






SELECT   dept                                        ANSWER
        ,SUM(years) OVER(PARTITION BY dept) AS sum   =================
        ,AVG(years) OVER(PARTITION BY dept) AS avg   DEPT  SUM AVG ROW
        ,COUNT(*)   OVER(PARTITION BY dept) AS row   ----- --- --- ---
FROM     staff                                          15  22   7   3
WHERE    id BETWEEN 40 AND 120                          15  22   7   3
  AND    years IS NOT NULL                              15  22   7   3
ORDER BY dept;                                          38   6   6   1
                                                        42  13   6   2
                                                        42  13   6   2
Figure 266, Sample query using PARTITION






SELECT   DISTINCT                                               ANSWER
         dept                                        =================
        ,SUM(years) OVER(PARTITION BY dept) AS sum   DEPT  SUM AVG ROW
        ,AVG(years) OVER(PARTITION BY dept) AS avg   ----- --- --- ---
        ,COUNT(*)   OVER(PARTITION BY dept) AS row      15  22   7   3
FROM     staff                                          38   6   6   1
WHERE    id BETWEEN 40 AND 120                          42  13   6   2
  AND    years IS NOT NULL
ORDER BY dept;
Figure 267, Sample query using PARTITION and DISTINCT






Figure 268, Moving window definition syntax






SELECT   id  ,salary
        ,DEC(AVG(salary) OVER()                       ,7,2) AS avg_all
        ,DEC(AVG(salary) OVER(ORDER BY id)            ,7,2) AS avg_odr
        ,DEC(AVG(salary) OVER(ORDER BY id
                 ROWS BETWEEN UNBOUNDED PRECEDING
                          AND UNBOUNDED FOLLOWING)    ,7,2) AS avg_p_f
        ,DEC(AVG(salary) OVER(ORDER BY id
                 ROWS BETWEEN UNBOUNDED PRECEDING
                          AND CURRENT ROW)            ,7,2) AS avg_p_c
        ,DEC(AVG(salary) OVER(ORDER BY id
                 ROWS BETWEEN CURRENT ROW
                          AND UNBOUNDED FOLLOWING)    ,7,2) AS avg_c_f
        ,DEC(AVG(salary) OVER(ORDER BY id
                 ROWS BETWEEN 2 PRECEDING
                          AND 1 FOLLOWING)            ,7,2) AS avg_2_1
FROM     staff
WHERE    dept  IN  (15,20)
  AND    id     >   20
ORDER BY id;
                                                                ANSWER
    ==================================================================
    ID    SALARY  AVG_ALL  AVG_ODR  AVG_P_F  AVG_P_C  AVG_C_F  AVG_2_1
    --- -------- -------- -------- -------- -------- -------- --------
     50 80659.80 53281.11 80659.80 53281.11 80659.80 53281.11 78581.31
     70 76502.83 53281.11 78581.31 53281.11 78581.31 47805.37 66889.07
     80 43504.60 53281.11 66889.07 53281.11 66889.07 40631.01 60793.85
    110 42508.20 53281.11 60793.85 53281.11 60793.85 39673.15 51193.53
    170 42258.50 53281.11 57086.78 53281.11 57086.78 38255.62 40631.01
    190 34252.75 53281.11 53281.11 53281.11 53281.11 34252.75 39673.15
Figure 269, Different window sizes






SELECT   id
        ,SUM(id) OVER(ORDER BY id)                             AS sum1
        ,SUM(id) OVER(ORDER BY id  ROWS         1 PRECEDING)   AS sum2
        ,SUM(id) OVER(ORDER BY id  ROWS UNBOUNDED PRECEDING)   AS sum3
        ,SUM(id) OVER(ORDER BY id  ROWS CURRENT ROW)           AS sum4
        ,SUM(id) OVER(ORDER BY id  ROWS         2 FOLLOWING)   AS sum5
        ,SUM(id) OVER(ORDER BY id  ROWS UNBOUNDED FOLLOWING)   AS sum6
FROM     staff
WHERE    id < 40                                                ANSWER
ORDER BY id;                          ================================
                                      ID SUM1 SUM2 SUM3 SUM4 SUM5 SUM6
                                      -- ---- ---- ---- ---- ---- ----
                                      10   10   10   10   10   60   60
                                      20   30   30   30   20   50   50
                                      30   60   50   60   30   30   30
Figure 270, Different window sizes






SELECT   id
        ,SMALLINT(SUM(id)  OVER(ORDER BY id
             RANGE BETWEEN 10 PRECEDING AND 10 FOLLOWING))     AS rng1
        ,SMALLINT(SUM(id)  OVER(ORDER BY id
             ROWS  BETWEEN  1 PRECEDING AND  1 FOLLOWING))     AS row1
        ,SMALLINT(SUM(id)  OVER(ORDER BY id
             RANGE BETWEEN 10 PRECEDING AND CURRENT ROW))      AS rng2
        ,SMALLINT(SUM(id)  OVER(ORDER BY id
             ROWS  BETWEEN  3 PRECEDING AND 1 PRECEDING))      AS row2
        ,SMALLINT(SUM(id)  OVER(ORDER BY id DESC
             ROWS  BETWEEN  3 PRECEDING AND 1 PRECEDING))      AS row3
        ,SMALLINT(SUM(id)  OVER(ORDER BY id
             RANGE  BETWEEN   UNBOUNDED PRECEDING
                        AND   20 FOLLOWING))                   AS rng3
FROM     staff
WHERE    id < 60
ORDER BY id;                                                    ANSWER
                                      ================================
                                      ID RNG1 ROW1 RNG2 ROW2 ROW3 RNG3
                                      -- ---- ---- ---- ---- ---- ----
                                      10   30   30   10    -   90   60
                                      20   60   60   30   10  120  100
                                      30   90   90   50   30   90  150
                                      40  120  120   70   60   50  150
                                      50   90   90   90   90    -  150
Figure 271, ROW vs. RANGE example






Figure 272, ORDER BY syntax






SELECT   dept ,name  ,salary
        ,DEC(SUM(salary)  OVER(ORDER BY dept)            ,8,2) AS sum1
        ,DEC(SUM(salary)  OVER(ORDER BY dept DESC)       ,8,2) AS sum2
        ,DEC(SUM(salary)  OVER(ORDER BY ORDER OF s1)     ,8,2) AS sum3
        ,SMALLINT(RANK()  OVER(ORDER BY salary, name, dept)  ) AS r1
        ,SMALLINT(RANK()  OVER(ORDER BY ORDER OF s1)         ) AS r2
        ,ROW_NUMBER()     OVER(ORDER BY salary)                AS w1
        ,COUNT(*)         OVER(ORDER BY salary)                AS w2
FROM    (SELECT   *
         FROM     staff
         WHERE    id < 60
         ORDER BY dept
                 ,name
        )AS s1
ORDER BY 1, 2;
       
                                                                ANSWER
      ================================================================
      DEPT NAME       SALARY      SUM1      SUM2      SUM3 R1 R2 W1 W2
      ---- -------- -------- --------- --------- --------- -- -- -- --
        15 Hanes    80659.80  80659.80 412701.30  80659.80  4  1  4  4
        20 Pernal   78171.25 257188.55 332041.50 158831.05  3  2  3  3
        20 Sanders  98357.50 257188.55 332041.50 257188.55  5  3  5  5
        38 Marenghi 77506.75 412701.30 155512.75 334695.30  1  4  1  1
        38 O'Brien  78006.00 412701.30 155512.75 412701.30  2  5  2  2
Figure 273, ORDER BY example






SELECT   id                           SELECT   id
        ,name                                 ,name
        ,ROW_NUMBER() OVER(                   ,ROW_NUMBER() OVER(
         ORDER BY ORDER OF s) od               ORDER BY ORDER OF s) od
FROM    (SELECT   *                   FROM    (SELECT   *
         FROM     staff                        FROM     staff
         WHERE    id  < 50                     WHERE    id  < 50
         ORDER BY name ASC                     ORDER BY name DESC
        )AS s                                 )AS s
ORDER BY id ASC;                      ORDER BY id ASC;
       
       
ANSWER                                ANSWER
===============                       ===============
ID NAME      OD                       ID NAME      OD
-- --------- --                       -- --------- --
10 Sanders    4                       10 Sanders    1
20 Pernal     3                       20 Pernal     2
30 Marenghi   1                       30 Marenghi   4
40 O'Brien    2                       40 O'Brien    3
Figure 274, ORDER BY table designator examples






SELECT   id
        ,years                                               AS yr
        ,salary
        ,DENSE_RANK()  OVER(ORDER BY years ASC)              AS a
        ,DENSE_RANK()  OVER(ORDER BY years ASC  NULLS FIRST) AS af
        ,DENSE_RANK()  OVER(ORDER BY years ASC  NULLS LAST ) AS al
        ,DENSE_RANK()  OVER(ORDER BY years DESC)             AS d
        ,DENSE_RANK()  OVER(ORDER BY years DESC NULLS FIRST) AS df
        ,DENSE_RANK()  OVER(ORDER BY years DESC NULLS LAST ) AS dl
FROM     staff
WHERE    id     < 100
ORDER BY years
        ,salary;                                                ANSWER
                                    ==================================
                                    ID YR SALARY    A  AF AL  D  DF DL
                                    -- -- --------  -- -- --  -- -- --
                                    30  5 77506.75   1  2  1   6  6  5
                                    90  6 38001.75   2  3  2   5  5  4
                                    40  6 78006.00   2  3  2   5  5  4
                                    70  7 76502.83   3  4  3   4  4  3
                                    10  7 98357.50   3  4  3   4  4  3
                                    20  8 78171.25   4  5  4   3  3  2
                                    50 10 80659.80   5  6  5   2  2  1
                                    80  - 43504.60   6  1  6   1  1  6
                                    60  - 66808.30   6  1  6   1  1  6
Figure 275, Overriding the default null ordering sequence






SELECT   COUNT(DISTINCT years) AS y#1
        ,MAX(y#)               AS y#2
FROM    (SELECT   years
                 ,DENSE_RANK()  OVER(ORDER BY years) AS y#
         FROM     staff
         WHERE    id     < 100
        )AS xxx                                                ANSWER
ORDER BY 1;                                                    =======
                                                               Y#1 Y#2
                                                               --- ---
                                                                 5   6
Figure 276, Counting distinct values - comparison






Figure 277, Ranking functions syntax






SELECT   id
        ,years
        ,salary
        ,RANK()       OVER(ORDER BY years) AS rank#
        ,DENSE_RANK() OVER(ORDER BY years) AS dense#
        ,ROW_NUMBER() OVER(ORDER BY years) AS row#
FROM     staff
WHERE    id     < 100                                           ANSWER
  AND    years  < 10               ===================================
ORDER BY years;                    ID YEARS SALARY   RANK# DENSE# ROW#
                                   -- ----- -------- ----- ------ ----
                                   30     5 77506.75     1      1    1
                                   40     6 78006.00     2      2    2
                                   90     6 38001.75     2      2    3
                                   10     7 98357.50     4      3    4
                                   70     7 76502.83     4      3    5
                                   20     8 78171.25     6      4    6
Figure 278, Ranking functions example






SELECT   job                                                 AS job
        ,years                                               AS yr
        ,id                                                  AS id
        ,name                                                AS name
        ,RANK() OVER(ORDER BY job ASC                      ) AS a1
        ,RANK() OVER(ORDER BY job ASC,  years ASC          ) AS a2
        ,RANK() OVER(ORDER BY job ASC,  years ASC  ,id ASC ) AS a3
        ,RANK() OVER(ORDER BY job DESC                     ) AS d1
        ,RANK() OVER(ORDER BY job DESC, years DESC         ) AS d2
        ,RANK() OVER(ORDER BY job DESC, years DESC, id DESC) AS d3
        ,RANK() OVER(ORDER BY job ASC,  years DESC, id ASC ) AS m1
        ,RANK() OVER(ORDER BY job DESC, years ASC,  id DESC) AS m2
FROM     staff
WHERE    id      <  150
  AND    years  IN (6,7)                                        ANSWER
  AND    job     >  'L'    ===========================================
ORDER BY job               JOB   YR ID  NAME    A1 A2 A3 D1 2 D3 M1 M2
        ,years             ----- -- --- ------- -- -- -- -- - -- -- --
        ,id;               Mgr    6 140 Fraye    1  1  1  4 6  6  3  4
                           Mgr    7  10 Sanders  1  2  2  4 4  5  1  6
                           Mgr    7 100 Plotz    1  2  3  4 4  4  2  5
                           Sales  6  40 O'Brien  4  4  4  1 2  3  5  2
                           Sales  6  90 Koonitz  4  4  5  1 2  2  6  1
                           Sales  7  70 Rothman  4  6  6  1 1  1  4  3
Figure 279, ORDER BY usage






SELECT   id                                          ANSWER
        ,years  AS yr                                =================
        ,salary                                      ID YR SALARY   R1
        ,RANK() OVER(PARTITION BY years              -- -- -------- --
                     ORDER     BY salary) AS r1      30  5 77506.75  1
FROM     staff                                       40  6 78006.00  1
WHERE    id     < 80                                 70  7 76502.83  1
  AND    years IS NOT NULL                           10  7 98357.50  2
ORDER BY years                                       20  8 78171.25  1
        ,salary;                                     50  0 80659.80  1
Figure 280, Values ranked by subset of rows






SELECT   id
        ,years
        ,salary
        ,SMALLINT(RANK() OVER(ORDER BY years ASC))  AS rank_a
        ,SMALLINT(RANK() OVER(ORDER BY years DESC)) AS rank_d
        ,SMALLINT(RANK() OVER(ORDER BY id, years))  AS rank_iy
FROM     STAFF
WHERE    id     < 100
  AND    years IS NOT NULL
ORDER BY years;
Figure 281, Multiple rankings in same query






SELECT   id
        ,years
        ,name
        ,salary
        ,SMALLINT(RANK() OVER(ORDER BY SUBSTR(name,3,2))) AS dumb1
        ,SMALLINT(RANK() OVER(ORDER BY salary / 1000))    AS dumb2
        ,SMALLINT(RANK() OVER(ORDER BY years * ID))       AS dumb3
        ,SMALLINT(RANK() OVER(ORDER BY 1))                AS dumb4
FROM     staff
WHERE    id     < 40
  AND    years IS NOT NULL
ORDER BY 1;
Figure 282, Dumb rankings, SQL






ID  YEARS  NAME      SALARY    DUMB1  DUMB2  DUMB3  DUMB4
--  -----  --------  --------  -----  -----  -----  -----
10      7  Sanders   98357.50      1      3      1      1
20      8  Pernal    78171.25      3      2      3      1
30      5  Marenghi  77506.75      2      1      2      1
Figure 283, Dumb ranking, Answer






SELECT   xxx.*                                        ANSWER
        ,RANK()OVER(ORDER BY id) AS r2                ================
FROM    (SELECT   id                                  ID NAME    R1 R2
                 ,name                                -- ------- -- --
                 ,RANK() OVER(ORDER BY id) AS r1      40 O'Brien  4  1
         FROM     staff                               50 Hanes    5  2
         WHERE    id     < 100                        70 Rothman  6  3
           AND    years IS NOT NULL                   90 Koonitz  7  4
        )AS xxx
WHERE    id > 30
ORDER BY id;
Figure 284, Subsequent processing of ranked data






SELECT   id                                          ANSWER
        ,RANK() OVER(PARTITION BY dept               =================
                     ORDER BY salary DESC) AS r1     ID R1 SALARY   DP
        ,salary                                      -- -- -------- --
        ,dept AS dp                                  10  1 98357.50 20
FROM     staff                                       50  1 80659.80 15
WHERE    id     < 80                                 40  1 78006.00 38
  AND    years IS NOT NULL                           20  2 78171.25 20
ORDER BY r1     ASC                                  30  2 77506.75 38
        ,salary DESC;                                70  2 76502.83 15
Figure 285, Ordering rows by rank, using RANK function






SELECT   id                                          ANSWER
        ,(SELECT COUNT(*)                            =================
          FROM   staff s2                            ID R1 SALARY   DP
          WHERE  s2.id        < 80                   -- -- -------- --
            AND  S2.YEARS IS NOT NULL                10  1 98357.50 20
            AND  s2.dept      = s1.dept              50  1 80659.80 15
            AND  s2.salary   >= s1.salary) AS R1     40  1 78006.00 38
        ,SALARY                                      20  2 78171.25 20
        ,dept AS dp                                  30  2 77506.75 38
FROM     staff s1                                    70  2 76502.83 15
WHERE    id     < 80
  AND    years IS NOT NULL
ORDER BY r1     ASC
        ,salary DESC;
Figure 286, Ordering rows by rank, using sub-query






SELECT   id                                             ANSWER
        ,salary                                         ==============
        ,dept AS dp                                     ID SALARY   DP
FROM    (SELECT   s1.*                                  -- -------- --
                 ,RANK() OVER(PARTITION BY dept         50 80659.80 15
                         ORDER BY salary DESC) AS r1    10 98357.50 20
         FROM     staff s1                              40 78006.00 38
         WHERE    id     < 80
           AND    years IS NOT NULL
        )AS xxx
WHERE    r1 = 1
ORDER BY dp;
Figure 287, Get highest salary in each department, use RANK function






SELECT   id                                             ANSWER
        ,salary                                         ==============
        ,dept AS dp                                     ID SALARY   DP
FROM     staff s1                                       -- -------- --
WHERE    id     < 80                                    50 80659.80 15
  AND    years IS NOT NULL                              10 98357.50 20
  AND    NOT EXISTS                                     40 78006.00 38
        (SELECT *
         FROM   staff s2
         WHERE  s2.id         < 80
           AND  s2.years IS NOT NULL
           AND  s2.dept      = s1.dept
           AND  s2.salary    > s1.salary)
ORDER BY DP;
Figure 288, Get highest salary in each department, use correlated sub-query






SELECT   id                                             ANSWER
        ,salary                                         ==============
        ,dept AS dp                                     ID SALARY   DP
FROM     staff                                          -- -------- --
WHERE    id     < 80                                    50 80659.80 15
  AND    years IS NOT NULL                              10 98357.50 20
  AND   (dept, salary) IN                               40 78006.00 38
        (SELECT   dept, MAX(salary)
         FROM     staff
         WHERE    id         < 80
           AND    years IS NOT NULL
         GROUP BY dept)
ORDER BY dp;
Figure 289, Get highest salary in each department, use uncorrelated sub-query






Figure 290, Numbering function syntax






SELECT   id                                          ANSWER
        ,name                                        =================
        ,ROW_NUMBER() OVER()            AS r1        ID NAME     R1 R2
        ,ROW_NUMBER() OVER(ORDER BY id) AS r2        -- -------- -- --
FROM     staff                                       10 Sanders   1  1
WHERE    id     < 50                                 20 Pernal    2  2
  AND    years IS NOT NULL                           30 Marenghi  3  3
ORDER BY id;                                         40 O'Brien   4  4
Figure 291, ORDER BY example, 1 of 3






SELECT   id                                          ANSWER
        ,name                                        =================
        ,ROW_NUMBER() OVER()              AS r1      ID NAME     R1 R2
        ,ROW_NUMBER() OVER(ORDER BY name) AS r2      -- -------- -- --
FROM     staff                                       10 Sanders   4  4
WHERE    id     < 50                                 20 Pernal    3  3
  AND    years IS NOT NULL                           30 Marenghi  1  1
ORDER BY id;                                         40 O'Brien   2  2
Figure 292, ORDER BY example, 2 of 3






SELECT   id                                       ANSWER
        ,name                                     ====================
        ,ROW_NUMBER() OVER()              AS r1   ID NAME     R1 R2 R3
        ,ROW_NUMBER() OVER(ORDER BY ID)   AS r2   -- -------- -- -- --
        ,ROW_NUMBER() OVER(ORDER BY NAME) AS r3   10 Sanders   1  1  4
FROM     staff                                    20 Pernal    2  2  3
WHERE    id     < 50                              30 Marenghi  3  3  1
  AND    years IS NOT NULL                        40 O'Brien   4  4  2
ORDER BY id;
Figure 293, ORDER BY example, 3 of 3






SELECT   job
        ,years
        ,id
        ,name
        ,ROW_NUMBER() OVER(PARTITION BY job  ORDER BY years) AS row#
        ,RANK()       OVER(PARTITION BY job  ORDER BY years) AS rn1#
        ,DENSE_RANK() OVER(PARTITION BY job  ORDER BY years) AS rn2#
FROM     staff
WHERE    id      <  150                                         ANSWER
  AND    years  IN (6,7)        ======================================
  AND    job     >  'L'         JOB   YEARS ID  NAME    ROW# RN1# RN2#
ORDER BY job                    ----- ----- --- ------- ---- ---- ----
        ,years;                 Mgr       6 140 Fraye      1    1    1
                                Mgr       7  10 Sanders    2    2    2
                                Mgr       7 100 Plotz      3    2    2
                                Sales     6  40 O'Brien    1    1    1
                                Sales     6  90 Koonitz    2    1    1
                                Sales     7  70 Rothman    3    3    2
Figure 294, Use of PARTITION phrase






SELECT   *                                               ANSWER
FROM    (SELECT   id                                     =============
                 ,name                                   ID NAME     R
                 ,ROW_NUMBER() OVER(ORDER BY id) AS r    -- -------- -
         FROM     staff                                  10 Sanders  1
         WHERE    id     < 100                           20 Pernal   2
           AND    years IS NOT NULL                      30 Marenghi 3
        )AS xxx
WHERE    r <= 3
ORDER BY id;
Figure 295, Select first 3 rows, using ROW_NUMBER function






SELECT   id                                              ANSWER
        ,name                                            =============
        ,ROW_NUMBER() OVER(ORDER BY id) AS r             ID NAME     R
FROM     staff                                           -- -------- -
WHERE    id     < 100                                    10 Sanders  1
  AND    years IS NOT NULL                               20 Pernal   2
ORDER BY id                                              30 Marenghi 3
FETCH FIRST 3 ROWS ONLY;
Figure 296, Select first 3 rows, using FETCH FIRST notation






SELECT   *                                               ANSWER
FROM    (SELECT   id                                     =============
                 ,name                                   ID NAME     R
                 ,ROW_NUMBER() OVER(ORDER BY id) AS r    -- -------- -
         FROM     staff                                  30 Marenghi 3
         WHERE    id         < 200                       40 O'Brien  4
           AND    years IS NOT NULL                      50 Hanes    5
        )AS xxx                                          70 Rothman  6
WHERE    r BETWEEN 3 AND 6
ORDER BY id;
Figure 297, Select 3rd through 6th rows






SELECT   *                                              ANSWER
FROM    (SELECT   id                                    ==============
                 ,name                                  ID  NAME    R
                 ,ROW_NUMBER() OVER(ORDER BY id) AS r   --- ------- --
         FROM     staff                                  10 Sanders  1
         WHERE    id         < 200                       70 Rothman  6
           AND    years IS NOT NULL                     140 Fraye   11
        )AS xxx                                         190 Sneider 16
WHERE    (r - 1) = ((r - 1) / 5) * 5
ORDER BY id;
Figure 298, Select every 5th matching row






SELECT   *
FROM    (SELECT   id
                 ,name
                 ,ROW_NUMBER() OVER(ORDER BY id DESC) AS r
         FROM     staff                                         ANSWER
         WHERE    id         < 200                      ==============
           AND    years IS NOT NULL                     ID  NAME     R
        )AS xxx                                         --- -------- -
WHERE    r <= 2                                         180 Abrahams 2
ORDER BY id;                                            190 Sneider  1
Figure 299, Select last two rows






SELECT   *
FROM    (SELECT  years
                ,id
                ,name
                ,RANK()       OVER(ORDER BY years)     AS rnk
                ,ROW_NUMBER() OVER(ORDER BY years, id) AS row
         FROM    staff
         WHERE   id         < 200                               ANSWER
           AND   years IS NOT NULL          ==========================
        )AS xxx                             YEARS ID  NAME     RNK ROW
WHERE    rnk  <= 3                          ----- --- -------- --- ---
ORDER BY years                                  3 180 Abrahams   1   1
        ,id;                                    4 170 Kermisch   2   2
                                                5  30 Marenghi   3   3
                                                5 110 Ngan       3   4
Figure 300, Select first "n" rows, or more if needed






CREATE TABLE invoice
(inv#        INTEGER        NOT NULL
,customer#   INTEGER        NOT NULL
,sale_date   DATE           NOT NULL
,sale_value  DECIMAL(9,2)   NOT NULL
,CONSTRAINT ctx1 PRIMARY KEY (inv#)
,CONSTRAINT ctx2 CHECK(inv# >= 0));
Figure 301, Performance test table - definition






INSERT INTO invoice
WITH temp (n,m) AS
(VALUES   (INTEGER(0),RAND(1))
 UNION ALL
 SELECT  n+1, RAND()
 FROM    temp
 WHERE   n+1 < 1000000
)      
SELECT n                                 AS inv#
      ,INT(m * 1000)                     AS customer#
      ,DATE('2000-11-01') + (m*40) DAYS  AS sale_date
      ,DECIMAL((m * m * 100),8,2)        AS sale_value
FROM   temp;
Figure 302, Performance test table - insert 1,000,000 rows






SELECT   s.*
FROM     invoice s
ORDER BY inv#
FETCH FIRST 5 ROWS ONLY;
Figure 303, Fetch first 5 rows - 0.000 elapsed seconds






SELECT   s.*
FROM     invoice s
ORDER BY inv#
FETCH FIRST 5 ROWS ONLY
OPTIMIZE FOR 5 ROWS;
Figure 304, Fetch first 5 rows - 0.000 elapsed seconds






SELECT   s.*
        ,ROW_NUMBER() OVER() AS row#
FROM     invoice s
ORDER BY inv#
FETCH FIRST 5 ROWS ONLY;
Figure 305, Fetch first 5 rows+ number rows - 0.000 elapsed seconds






SELECT   *
FROM    (SELECT   s.*
                 ,ROW_NUMBER() OVER() AS row#
         FROM     invoice s
        )xxx
ORDER BY inv#
FETCH FIRST 5 ROWS ONLY;
Figure 306, Fetch first 5 rows+ number rows - 0.000 elapsed seconds






SELECT   *
FROM    (SELECT   s.*
                 ,ROW_NUMBER() OVER() AS row#
         FROM     invoice s
        )xxx
WHERE    row# <= 5
ORDER BY inv#;
Figure 307, Process and number all rows - 0.049 elapsed seconds






SELECT   *
FROM    (SELECT   s.*
                 ,ROW_NUMBER() OVER(ORDER BY inv#) AS row#
         FROM     invoice s
        )xxx
WHERE    row# <= 5
ORDER BY inv#;
Figure 308, Process and number 5 rows only - 0.000 elapsed seconds






WITH temp (inv#, c#, sd, sv, n) AS
  (SELECT  inv.*
          ,1
   FROM    invoice inv
   WHERE   inv# =
          (SELECT MIN(inv#)
           FROM   invoice)
   UNION   ALL
   SELECT  new.*, n + 1
   FROM    temp    old
          ,invoice new
   WHERE   old.inv# < new.inv#
     AND   old.n    < 5
     AND   new.inv# =
          (SELECT MIN(xxx.inv#)
           FROM   invoice xxx
           WHERE  xxx.inv# > old.inv#)
  )    
SELECT   *
FROM     temp;
Figure 309, Fetch first 5 rows - 0.000 elapsed seconds






Figure 310, Function syntax






SELECT   dept ,id ,name                                         ANSWER
        ,FIRST_VALUE(name)                  ==========================
           OVER(PARTITION BY dept           DEPT  ID NAME     FRST
                ORDER BY id) AS frst        ---- --- -------- --------
FROM     staff                                10 210 Lu       Lu
WHERE    dept <= 15                           10 240 Daniels  Lu
  AND    id    > 160                          10 260 Jones    Lu
ORDER BY dept ,id;                            15 170 Kermisch Kermisch
Figure 311, FIRST_NAME function example






SELECT   dept ,id ,comm
        ,FIRST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm)             AS first1
        ,FIRST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm NULLS FIRST) AS first2
        ,FIRST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm NULLS LAST)  AS first3
        ,FIRST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm NULLS LAST
                ROWS BETWEEN 1 PRECEDING AND CURRENT ROW)    AS first4
        ,LAST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm)             AS last1
        ,LAST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm NULLS FIRST
                ROWS UNBOUNDED FOLLOWING)                    AS last2
FROM     staff
WHERE    id    < 100
  AND    dept  < 30
ORDER BY dept ,comm;
                                                                ANSWER
     =================================================================
     DEPT ID    COMM  FIRST1  FIRST2  FIRST3  FIRST4   LAST1     LAST2
     ---- -- ------- ------- ------- ------- ------- ------- ---------
       15 70 1152.00 1152.00       - 1152.00 1152.00 1152.00   1152.00
       15 50       - 1152.00       - 1152.00 1152.00       -   1152.00
       20 80  128.20  128.20       -  128.20  128.20  128.20    612.45
       20 20  612.45  128.20       -  128.20  128.20  612.45    612.45
       20 10       -  128.20       -  128.20  612.45       -    612.45
Figure 312, Function examples






SELECT   dept ,id ,comm
        ,FIRST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm)             AS rn_lst
        ,FIRST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm NULLS LAST)  AS rn_ls2
        ,FIRST_VALUE(comm)
           OVER(PARTITION BY dept ORDER BY comm NULLS FIRST) AS rn_fst
        ,FIRST_VALUE(comm,'IGNORE NULLS')
           OVER(PARTITION BY dept ORDER BY comm NULLS FIRST) AS in_fst
FROM     staff
WHERE    id   BETWEEN 20 AND 160
  AND    dept      <= 20                                        ANSWER
ORDER BY dept ,comm;      ============================================
                          DEPT  ID   COMM  RN_LST RN_LS2 RN_FST IN_FST
                          ---- --- ------- ------ ------ ------ ------
                            10 160       -      -      -      -      -
                            15 110  206.60 206.60 206.60      - 206.60
                            15  70 1152.00 206.60 206.60      - 206.60
                            15  50       - 206.60 206.60      -      -
                            20  80  128.20 128.20 128.20 128.20 128.20
                            20  20  612.45 128.20 128.20 128.20 128.20
Figure 313, Null value processing






Figure 314, Function syntax






SELECT   dept ,id ,comm
        ,LAG(comm)    OVER(PARTITION BY dept ORDER BY comm)    AS lag1
        ,LAG(comm,0)  OVER(PARTITION BY dept ORDER BY comm)    AS lag2
        ,LAG(comm,2)  OVER(PARTITION BY dept ORDER BY comm)    AS lag3
        ,LAG(comm,1,-1,'IGNORE NULLS')
                      OVER(PARTITION BY dept ORDER BY comm)    AS lag4
        ,LEAD(comm)   OVER(PARTITION BY dept ORDER BY comm)    AS led1
FROM     staff
WHERE    id   BETWEEN 20 AND 160
  AND    dept      <= 20
ORDER BY dept ,comm;
                                                                ANSWER
              ========================================================
              DEPT  ID    COMM    LAG1    LAG2    LAG3    LAG4    LED1
              ---- --- ------- ------- ------- ------- ------- -------
                10 160       -       -       -       -   -1.00       -
                15 110  206.60       -  206.60       -   -1.00 1152.00
                15  70 1152.00  206.60 1152.00       -  206.60       -
                15  50       - 1152.00       -  206.60 1152.00       -
                20  80  128.20       -  128.20       -   -1.00  612.45
                20  20  612.45  128.20  612.45       -  128.20       -
Figure 315, LAG and LEAD function Examples






Figure 316, Aggregation function syntax






SELECT   id ,name ,salary
        ,SUM(salary) OVER() AS sum_sal
        ,AVG(salary) OVER() AS avg_sal
        ,MIN(salary) OVER() AS min_sal
        ,MAX(salary) OVER() AS max_sal
        ,COUNT(*)    OVER() AS #rows
FROM     staff
WHERE    id < 30
ORDER BY id;
                                                                ANSWER
        ==============================================================
        ID NAME       SALARY   SUM_SAL  AVG_SAL  MIN_SAL MAX_SAL #ROWS
        -- -------- -------- --------- -------- -------- -------- ----
        10 Sanders  98357.50 254035.50 84678.50 77506.75 98357.50    3
        20 Pernal   78171.25 254035.50 84678.50 77506.75 98357.50    3
        30 Marenghi 77506.75 254035.50 84678.50 77506.75 98357.50    3
Figure 317, Aggregation function, basic usage






SELECT   id
        ,name
        ,salary
        ,SUM(salary) OVER()                                  AS sum1
        ,SUM(salary) OVER(ORDER BY id * 0)                   AS sum2
        ,SUM(salary) OVER(ORDER BY 'ABC')                    AS sum3
        ,SUM(salary) OVER(ORDER BY 'ABC'
                          RANGE BETWEEN UNBOUNDED PRECEDING
                                    AND UNBOUNDED FOLLOWING) AS sum4
FROM     staff
WHERE    id < 60
ORDER BY id;
       
                                                                ANSWER
          ============================================================
          ID NAME       SALARY      SUM1      SUM2      SUM3      SUM4
          -- -------- -------- --------- --------- --------- ---------
          10 Sanders  98357.50 412701.30 412701.30 412701.30 412701.30
          20 Pernal   78171.25 412701.30 412701.30 412701.30 412701.30
          30 Marenghi 77506.75 412701.30 412701.30 412701.30 412701.30
          40 O'Brien  78006.00 412701.30 412701.30 412701.30 412701.30
          50 Hanes    80659.80 412701.30 412701.30 412701.30 412701.30
Figure 318, Logically equivalent aggregation functions






SELECT   dept
        ,name
        ,salary
        ,SUM(salary)  OVER(ORDER BY dept)                 AS sum1
        ,SUM(salary)  OVER(ORDER BY dept DESC)            AS sum2
        ,SUM(salary)  OVER(ORDER BY dept, NAME)           AS sum3
        ,SUM(salary)  OVER(ORDER BY dept DESC, name DESC) AS sum4
        ,COUNT(*)     OVER(ORDER BY dept)                 AS rw1
        ,COUNT(*)     OVER(ORDER BY dept, NAME)           AS rw2
FROM     staff
WHERE    id < 60
ORDER BY dept
        ,name;
       
                                                                ANSWER
======================================================================
DEPT NAME       SALARY      SUM1      SUM2      SUM3      SUM4 RW1 RW2
---- -------- -------- --------- --------- --------- --------- --- ---
  15 Hanes    80659.80  80659.80 412701.30  80659.80 412701.30   1   1
  20 Pernal   78171.25 257188.55 332041.50 158831.05 332041.50   3   2
  20 Sanders  98357.50 257188.55 332041.50 257188.55 253870.25   3   3
  38 Marenghi 77506.75 412701.30 155512.75 334695.30 155512.75   5   4
  38 O'Brien  78006.00 412701.30 155512.75 412701.30  78006.00   5   5
Figure 319, Aggregation function, ORDER BY usage






SELECT   id  ,years
        ,AVG(years) OVER()                             AS "p_f"
        ,AVG(years) OVER(ORDER BY id
                    ROWS BETWEEN UNBOUNDED PRECEDING
                             AND UNBOUNDED FOLLOWING)  AS "p_f"
        ,AVG(years) OVER(ORDER BY id)                  AS "p_c"
        ,AVG(years) OVER(ORDER BY id
                    ROWS BETWEEN UNBOUNDED PRECEDING
                             AND CURRENT ROW)          AS "p_c"
        ,AVG(years) OVER(ORDER BY id
                    ROWS         UNBOUNDED PRECEDING)  AS "p_c"
        ,AVG(years) OVER(ORDER BY id
                    ROWS         UNBOUNDED FOLLOWING)  AS "c_f"
        ,AVG(years) OVER(ORDER BY id
                    ROWS         2 FOLLOWING)          AS "c_2"
        ,AVG(years) OVER(ORDER BY id
                    ROWS         1 PRECEDING)          AS "1_c"
        ,AVG(years) OVER(ORDER BY id
                    ROWS BETWEEN 1 FOLLOWING
                             AND 2 FOLLOWING)          AS "1_2"
FROM     staff
WHERE    dept  IN  (15,20)
  AND    id     >   20                                          ANSWER
  AND    years  >   1    =============================================
ORDER BY id;              ID YEARS p_f p_f p_c p_c p_c c_f c_2 1_c 1_2
                         --- ----- --- --- --- --- --- --- --- --- ---
                          50    10   6   6  10  10  10   6   7  10   6
                          70     7   6   6   8   8   8   6   5   8   4
                         110     5   6   6   7   7   7   5   5   6   6
                         170     4   6   6   6   6   6   6   6   4   8
                         190     8   6   6   6   6   6   8   8   6   -
Figure 320, ROWS usage examples






SELECT   dept
        ,name
        ,years
        ,SMALLINT(SUM(years) OVER(ORDER BY dept
                             ROWS  BETWEEN 1 PRECEDING
                                       AND CURRENT ROW))   AS row1
        ,SMALLINT(SUM(years) OVER(ORDER BY dept
                             ROWS  BETWEEN 2 PRECEDING
                                       AND CURRENT ROW))   AS row2
        ,SMALLINT(SUM(years) OVER(ORDER BY dept
                             RANGE BETWEEN 1 PRECEDING
                                       AND CURRENT ROW))   AS rg01
        ,SMALLINT(SUM(years) OVER(ORDER BY dept
                             RANGE BETWEEN 10 PRECEDING
                                       AND CURRENT ROW))   AS rg10
        ,SMALLINT(SUM(years) OVER(ORDER BY dept
                             RANGE BETWEEN 20 PRECEDING
                                       AND CURRENT ROW))   AS rg20
        ,SMALLINT(SUM(years) OVER(ORDER BY dept
                             RANGE BETWEEN 10 PRECEDING
                                       AND 20 FOLLOWING))  AS rg11
        ,SMALLINT(SUM(years) OVER(ORDER BY dept
                             RANGE BETWEEN CURRENT ROW
                                       AND 20 FOLLOWING))  AS rg99
FROM     staff
WHERE    id         < 100
  AND    years IS NOT NULL
ORDER BY dept
        ,name;
                                                                ANSWER
               =======================================================
               DEPT   NAME    YEARS ROW1 ROW2 RG01 RG10 RG20 RG11 RG99
               ------ ------- ----- ---- ---- ---- ---- ---- ---- ----
                   15 Hanes      10   10   10   17   17   17   32   32
                   15 Rothman     7   17   17   17   17   17   32   32
                   20 Pernal      8   15   25   15   32   32   43   26
                   20 Sanders     7   15   22   15   32   32   43   26
                   38 Marengh     5   12   20   11   11   26   17   17
                   38 O'Brien     6   11   18   11   11   26   17   17
                   42 Koonitz     6   12   17    6   17   17   17    6
Figure 321, RANGE usage






SELECT   id
        ,name
        ,SMALLINT(SUM(id) OVER(ORDER BY id ASC
                          ROWS BETWEEN 1 PRECEDING
                                   AND CURRENT ROW)) AS apc
        ,SMALLINT(SUM(id) OVER(ORDER BY id ASC
                          ROWS BETWEEN CURRENT ROW
                                   AND 1 FOLLOWING)) AS acf
        ,SMALLINT(SUM(id) OVER(ORDER BY id DESC
                          ROWS BETWEEN 1 PRECEDING
                                   AND CURRENT ROW)) AS dpc
        ,SMALLINT(SUM(id) OVER(ORDER BY id DESC
                          ROWS BETWEEN CURRENT ROW
                                   AND 1 FOLLOWING)) AS dcf
FROM     staff
WHERE    id         < 50
  AND    years IS NOT NULL                 ANSWER
ORDER BY id;                               ===========================
                                           ID NAME     APC ACF DPC DCF
                                           -- -------- --- --- --- ---
                                           10 Sanders   10  30  30  10
                                           20 Pernal    30  50  50  30
                                           30 Marenghi  50  70  70  50
                                           40 O'Brien   70  40  40  70
Figure 322,BETWEEN and ORDER BY usage






ASC id (10,20,30,40)
READ ROWS, LEFT to RIGHT     1ST-ROW    2ND-ROW    3RD-ROW    4TH-ROW
==========================   ========   ========   ========   ========
1 PRECEDING to CURRENT ROW      10=10   10+20=30   20+30=40   30+40=70
CURRENT ROW to 1 FOLLOWING   10+20=30   20+30=50   30+40=70   40   =40
       
       
DESC id (40,30,20,10)
READ ROWS, RIGHT to LEFT     1ST-ROW    2ND-ROW    3RD-ROW    4TH-ROW
==========================   ========   ========   ========   ========
1 PRECEDING to CURRENT ROW   20+10=30   30+20=50   40+30=70   40   =40
CURRENT ROW to 1 FOLLOWING   10   =10   20+10=30   30+20=50   40+30=70
       
       
NOTE: Preceding row is always on LEFT of current row.
      Following row is always on RIGHT of current row.
Figure 323, Explanation of query






CREATE VIEW scalar (d1,f1,s1,c1,v1,ts1,dt1,tm1,tc1) AS
WITH temp1 (n1, c1, t1) AS
(VALUES  (-2.4,'ABCDEF','1996-04-22-23.58.58.123456')
        ,(+0.0,'ABCD  ','1996-08-15-15.15.15.151515')
        ,(+1.8,'AB    ','0001-01-01-00.00.00.000000'))
SELECT DECIMAL(n1,3,1)
      ,DOUBLE(n1)
      ,SMALLINT(n1)
      ,CHAR(c1,6)
      ,VARCHAR(RTRIM(c1),6)
      ,TIMESTAMP(t1)
      ,DATE(t1)
      ,TIME(t1)
      ,CHAR(t1)
FROM   temp1;
Figure 324, Sample View DDL - Scalar functions






D1       F1          S1   C1       V1       TS1
------   ---------   --   ------   ------   --------------------------
  -2.4   -2.4e+000   -2   ABCDEF   ABCDEF   1996-04-22-23.58.58.123456
   0.0    0.0e+000    0   ABCD     ABCD     1996-08-15-15.15.15.151515
   1.8    1.8e+000    1   AB       AB       0001-01-01-00.00.00.000000
       
       
DT1          TM1        TC1
----------   --------   --------------------------
1996-04-22   23:58:58   1996-04-22-23.58.58.123456
1996-08-15   15:15:15   1996-08-15-15.15.15.151515
0001-01-01   00:00:00   0001-01-01-00.00.00.000000
Figure 325, SCALAR view, contents (3 rows)






SELECT d1      AS d1                  ANSWER (float output shortened)
      ,ABS(D1) AS d2                  ================================
      ,f1      AS f1                  D1    D2   F1          F2
      ,ABS(f1) AS f2                  ----  ---  ----------  ---------
FROM   scalar;                        -2.4  2.4   -2.400e+0  2.400e+00
                                       0.0  0.0    0.000e+0  0.000e+00
                                       1.8  1.8    1.800e+0  1.800e+00
Figure 326, ABS function examples






SELECT c1                                             ANSWER
      ,ASCII(c1)           AS ac1                     ================
      ,ASCII(SUBSTR(c1,2)) AS ac2                     C1      AC1  AC2
FROM   scalar                                         ------  ---  ---
WHERE  c1 = 'ABCDEF';                                 ABCDEF   65   66
Figure 327, ASCII function examples






WITH temp (big) AS                                ANSWER
(VALUES BIGINT(1)                                 ====================
 UNION ALL                                        BIG
 SELECT big * 256                                 --------------------
 FROM   temp                                                         1
 WHERE  big < 1E16                                                 256
)                                                                65536
SELECT big                                                    16777216
FROM   temp;                                                4294967296
                                                         1099511627776
                                                       281474976710656
                                                     72057594037927936
Figure 328, BIGINT function example






WITH temp (f1) AS
(VALUES FLOAT(1.23456789)
 UNION ALL
 SELECT f1 * 100
 FROM   temp
 WHERE  f1 < 1E18
)      
SELECT f1         AS float1
      ,DEC(f1,19) AS decimal1
      ,BIGINT(f1) AS bigint1
FROM   temp;
Figure 329, Convert FLOAT to DECIMAL and BIGINT, SQL






FLOAT1                  DECIMAL1             BIGINT1
----------------------  -------------------  --------------------
+1.23456789000000E+000                    1.                    1
+1.23456789000000E+002                  123.                  123
+1.23456789000000E+004                12345.                12345
+1.23456789000000E+006              1234567.              1234567
+1.23456789000000E+008            123456789.            123456788
+1.23456789000000E+010          12345678900.          12345678899
+1.23456789000000E+012        1234567890000.        1234567889999
+1.23456789000000E+014      123456789000000.      123456788999999
+1.23456789000000E+016    12345678900000000.    12345678899999996
+1.23456789000000E+018  1234567890000000000.  1234567889999999488
Figure 330, Convert FLOAT to DECIMAL and BIGINT, answer






Figure 331, BIT functions syntax






WITH                                                            ANSWER
temp1 (b1, b2) AS                      ===============================
  (VALUES ( 1, 0)  ,( 0, 1)            B1 B2 hex1 hex2 and ano  or xor
         ,( 0, 0)  ,( 1, 1)            -- -- ---- ---- --- --- --- ---
         ,( 2, 1)  ,(15,-7)             1  0 0100 0000   0   1   1   1
         ,(15, 7)  ,(-1, 1)             0  1 0000 0100   0   0   1   1
         ,(15,63)  ,(63,31)             0  0 0000 0000   0   0   0   0
         ,(99,64)  ,( 0,-2)),           1  1 0100 0100   1   0   1   0
temp2 (b1, b2) AS                       2  1 0200 0100   0   2   3   3
  (SELECT   SMALLINT(b1)               15 -7 0F00 F9FF   9   6  -1 -10
           ,SMALLINT(b2)               15  7 0F00 0700   7   8  15   8
   FROM     temp1)                     -1  1 FFFF 0100   1  -2  -1  -2
SELECT   b1 ,b2                        15 63 0F00 3F00  15   0  63  48
        ,HEX(b1)           AS "hex1"   63 31 3F00 1F00  31  32  63  32
        ,HEX(b2)           AS "hex2"   99 64 6300 4000  64  35  99  35
        ,BITAND(b1,b2)     AS "and"     0 -2 0000 FEFF   0   0  -2  -2
        ,BITANDNOT(b1,b2)  AS "ano"
        ,BITOR(b1,b2)      AS "or"
        ,BITXOR(b1,b2)     AS "xor"
FROM     temp2;
Figure 332, BIT functions examples






CREATE FUNCTION bitdisplay(inparm SMALLINT)
RETURNS CHAR(16)
BEGIN ATOMIC
   DECLARE outstr VARCHAR(16);
   DECLARE inval  INT;
   IF inparm >= 0 THEN
      SET inval = inparm;
   ELSE
      SET inval = INT(65536) + inparm;
   END IF;
   SET outstr = '';
   WHILE inval > 0 DO
      SET outstr = STRIP(CHAR(MOD(inval,2))) || outstr;
      SET inval  = inval / 2;
   END WHILE;
   RETURN RIGHT(REPEAT('0',16) || outstr,16);
END!   
Figure 333, Function to display SMALLINT bits






WITH                                                            ANSWER
temp1 (b1) AS                             ============================
   (VALUES   (32767)  ,(16383)            B1     hex1 bit_display
            ,( 4096)  ,(  118)            ------ ---- ----------------
            ,(   63)  ,(   16)             32767 FF7F 0111111111111111
            ,(    2)  ,(    1)             16383 FF3F 0011111111111111
            ,(    0)  ,(   -1)              4096 0010 0001000000000000
            ,(   -2)  ,(   -3)               118 7600 0000000001110110
            ,(  -64)  ,(-32768)               63 3F00 0000000000111111
   ),                                         16 1000 0000000000010000
temp2 (b1) AS                                  2 0200 0000000000000010
   (SELECT   SMALLINT(b1)                      1 0100 0000000000000001
    FROM     temp1                             0 0000 0000000000000000
   )                                          -1 FFFF 1111111111111111
SELECT  b1                                    -2 FEFF 1111111111111110
       ,HEX(b1)        AS "hex1"              -3 FDFF 1111111111111101
       ,BITDISPLAY(b1) AS "bit_display"      -64 C0FF 1111111111000000
FROM    temp2;                            -32768 0080 1000000000000000
Figure 334, BIT_DISPLAY function example






WITH   
temp1 (b1) AS
   (VALUES (32767),(21845),( 4096),(    0),(   -1),(  -64)
   ),  
temp2 (b1, s15) AS
   (SELECT   SMALLINT(b1)
            ,SMALLINT(15)
    FROM     temp1
   )   
SELECT  b1
       ,BITDISPLAY(b1)                 AS "b1_display"
       ,BITXOR(b1,s15)                 AS "xor"
       ,BITDISPLAY(BITXOR(b1,s15))     AS "xor_display"
       ,BITANDNOT(b1,s15)              AS "andnot"
       ,BITDISPLAY(BITANDNOT(b1,s15))  AS "andnot_display"
FROM    temp2;
Figure 335, Update bits #1, query






B1     b1_display      xor    xor_display      andnot andnot_display
----- ---------------- ------ ---------------- ------ ----------------
32767 0111111111111111  32752 0111111111110000  32752 0111111111110000
21845 0101010101010101  21850 0101010101011010  21840 0101010101010000
 4096 0001000000000000   4111 0001000000001111   4096 0001000000000000
    0 0000000000000000     15 0000000000001111      0 0000000000000000
   -1 1111111111111111    -16 1111111111110000    -16 1111111111110000
  -64 1111111111000000    -49 1111111111001111    -64 1111111111000000
Figure 336, Update bits #1, answer






WITH   
temp1 (b1) AS
   (VALUES (32767),(21845),( 4096),(    0),(   -1),(  -64)
   ),  
temp2 (b1, s15) AS
   (SELECT   SMALLINT(b1)
            ,SMALLINT(15)
    FROM     temp1
   )   
SELECT  b1
       ,BITDISPLAY(b1)                 AS "b1_display"
       ,BITAND(b1,s15)                 AS "and"
       ,BITDISPLAY(BITAND(b1,s15))     AS "and_display"
       ,BITNOT(b1)                     AS "not"
       ,BITDISPLAY(BITNOT(b1))         AS "not_display"
FROM    temp2;
Figure 337, Update bits #2, query






B1     b1_display      and    and_display      not    not_display
----- ---------------- ------ ---------------- ------ ----------------
32767 0111111111111111     15 0000000000001111 -32768 1000000000000000
21845 0101010101010101      5 0000000000000101 -21846 1010101010101010
 4096 0001000000000000      0 0000000000000000  -4097 1110111111111111
    0 0000000000000000      0 0000000000000000     -1 1111111111111111
   -1 1111111111111111     15 0000000000001111      0 0000000000000000
  -64 1111111111000000      0 0000000000000000     63 0000000000111111
Figure 338, Update bits #2, answer






Figure 339, BLOB function syntax






Figure 340, CEILING function syntax






SELECT d1                           ANSWER  (float output shortened)
      ,CEIL(d1) AS d2               ==================================
      ,f1                           D1    D2    F1          F2
      ,CEIL(f1) AS f2               ----  ----  ----------  ----------
FROM   scalar;                      -2.4   -2.   -2.400E+0   -2.000E+0
                                     0.0    0.   +0.000E+0   +0.000E+0
                                     1.8    2.   +1.800E+0   +2.000E+0
Figure 341, CEIL function examples






Figure 342, CHAR function syntax






SELECT   name                    ANSWER
        ,CHAR(name,3)            =====================================
        ,comm                    NAME    2   COMM    4      5
        ,CHAR(comm)              ------- --- ------- -------- --------
        ,CHAR(comm,'@')          James   Jam  128.20 00128.20 00128@20
FROM     staff                   Koonitz Koo 1386.70 01386.70 01386@70
WHERE    id BETWEEN 80           Plotz   Plo       - -        -
                AND 100
ORDER BY id;
Figure 343, CHAR function examples - characters and numbers






                            ANSWER
                            ==========================================
                            INT      CHAR_INT CHAR_FLT    CHAR_DEC
                            -------- -------- ----------- ------------
WITH temp1 (n) AS                  3 3        3.0E0       00000000003.
(VALUES (3)                        9 9        9.0E0       00000000009.
 UNION ALL                        81 81       8.1E1       00000000081.
 SELECT  n * n                  6561 6561     6.561E3     00000006561.
 FROM    temp1              43046721 43046721 4.3046721E7 00043046721.
 WHERE   n < 9000
)      
SELECT  n               AS int
       ,CHAR(INT(n))    AS char_int
       ,CHAR(FLOAT(n))  AS char_flt
       ,CHAR(DEC(n))    AS char_dec
FROM    temp1;
Figure 344, CHAR function examples - positive numbers






WITH temp1 (n1, n2) AS             ANSWER
(VALUES (SMALLINT(+3)              ===================================
        ,SMALLINT(-7))             N1     I1    I2     D1      D2
 UNION ALL                         ------ ----- ------ ------- -------
 SELECT  n1 * n2                        3 3     +3     00003.  +00003.
        ,n2                           -21 -21   -21    -00021. -00021.
 FROM    temp1                        147 147   +147   00147.  +00147.
 WHERE   n1 < 300                   -1029 -1029 -1029  -01029. -01029.
)                                    7203 7203  +7203  07203.  +07203.
SELECT  n1
       ,CHAR(n1) AS i1
       ,CASE
           WHEN n1 < 0 THEN CHAR(n1)
           ELSE  '+' CONCAT CHAR(n1)
        END AS i2
       ,CHAR(DEC(n1)) AS d1
       ,CASE
           WHEN n1 < 0 THEN CHAR(DEC(n1))
           ELSE  '+' CONCAT CHAR(DEC(n1))
        END AS d2
FROM    temp1;
Figure 345, Align CHAR function output - numbers






                                                                ANSWER
                                                            ==========
SELECT   CHAR(CURRENT DATE,ISO)  AS iso            ==>      2005-11-30
        ,CHAR(CURRENT DATE,EUR)  AS eur            ==>      30.11.2005
        ,CHAR(CURRENT DATE,JIS)  AS jis            ==>      2005-11-30
        ,CHAR(CURRENT DATE,USA)  AS usa            ==>      11/30/2005
FROM     sysibm.sysdummy1;
Figure 346, CHAR function examples - date value






                                                                ANSWER
                                                              ========
SELECT   CHAR(CURRENT TIME,ISO)  AS iso            ==>        19.42.21
        ,CHAR(CURRENT TIME,EUR)  AS eur            ==>        19.42.21
        ,CHAR(CURRENT TIME,JIS)  AS jis            ==>        19:42:21
        ,CHAR(CURRENT TIME,USA)  AS usa            ==>        07:42 PM
FROM     sysibm.sysdummy1;
Figure 347, CHAR function examples - time value






SELECT   CHAR(CURRENT TIMESTAMP)                                ANSWER
FROM     sysibm.sysdummy1;                  ==========================
                                            2005-11-30-19.42.21.873002
Figure 348, CHAR function example - timestamp value






SELECT   d2                                           ANSWER
        ,CHAR(d2)    AS cd2                           ================
        ,DIGITS(d2)  AS dd2                           D2   CD2    DD2
FROM    (SELECT DEC(d1,4,1) AS d2                     ---- ------ ----
         FROM   scalar                                -2.4 -002.4 0024
        )AS xxx                                        0.0 000.0  0000
ORDER BY 1;                                            1.8 001.8  0018
Figure 349, DIGITS vs. CHAR






Figure 350, CHARACTER_LENGTH function syntax






WITH temp1 (c1) AS (VALUES (CAST('аи? AS VARCHAR(10))))
SELECT   c1                          AS C1
        ,LENGTH(c1)                  AS LEN
        ,OCTET_LENGTH(c1)            AS OCT                     ANSWER
        ,CHAR_LENGTH(c1,OCTETS)      AS L08    =======================
        ,CHAR_LENGTH(c1,CODEUNITS16) AS L16    C1  LEN OCT L08 L16 L32
        ,CHAR_LENGTH(c1,CODEUNITS32) AS L32    --- --- --- --- --- ---
FROM     temp1;                                аи?  6   6   6   3   3
Figure 351,CHARACTER_LENGTH function example






SELECT 'A'             AS "c"                        ANSWER
      ,ASCII('A')      AS "c>n"                      =================
      ,CHR(ASCII('A')) AS "c>n>c"                    C  C>N  C>N>C  NL
      ,CHR(333)        AS "nl"                       -  ---  -----  --
FROM   staff                                         A   65  A      Ъ
WHERE  id = 10;
Figure 352, CHR function examples






SELECT c1                                          ANSWER
      ,CLOB(c1)   AS cc1                           ===================
      ,CLOB(c1,3) AS cc2                           C1      CC1     CC2
FROM   scalar;                                     ------  ------  ---
                                                   ABCDEF  ABCDEF  ABC
                                                   ABCD    ABCD    ABC
                                                   AB      AB      AB
Figure 353, CLOB function examples






SELECT   id                                         ANSWER
        ,comm                                       ==================
        ,COALESCE(comm,0)                           ID  COMM    3
FROM     staff                                      --  ------  ------
WHERE    id < 30                                    10       -    0.00
ORDER BY id;                                        20  612.45  612.45
Figure 354, COALESCE function example






WITH temp1(c1,c2,c3) AS                                       ANSWER
(VALUES (CAST(NULL AS SMALLINT)                               ========
        ,CAST(NULL AS SMALLINT)                               CC1  CC2
        ,CAST(10   AS SMALLINT)))                             ---  ---
SELECT COALESCE(c1,c2,c3) AS cc1                               10   10
      ,CASE
          WHEN c1 IS NOT NULL THEN c1
          WHEN c2 IS NOT NULL THEN c2
          WHEN c3 IS NOT NULL THEN c3
       END AS cc2
FROM   TEMP1;
Figure 355, COALESCE and equivalent CASE expression






SELECT COUNT(*)             AS #rows               ANSWER
      ,MIN(id)              AS min_id              ===================
      ,COALESCE(MIN(id),-1) AS ccc_id              #ROWS MIN_ID CCC_ID
FROM   staff                                       ----- ------ ------
WHERE  id < 5;                                         0      -     -1
Figure 356, NOT NULL field returning null value






WITH temp1 (c1) As
   (VALUES ('a'),('A'),('?),('?),('b'))
SELECT   c1
        ,COLLATION_KEY_BIT(c1,'UCA400R1_S1',9) AS "a=A=??
        ,COLLATION_KEY_BIT(c1,'UCA400R1_S2',9) AS "a=A  4
Figure 420, MIN scalar function






SELECT   ts1                    ANSWER
        ,MINUTE(ts1)            ======================================
FROM     scalar                 TS1                        2
ORDER BY ts1;                   -------------------------- -----------
                                0001-01-01-00.00.00.000000           0
                                1996-04-22-23.58.58.123456          58
                                1996-08-15-15.15.15.151515          15
Figure 421, MINUTE function example






WITH temp1(n1,n2) AS                           ANSWER
(VALUES (-31,+11)                              =======================
 UNION ALL                                     N1   N2   DIV  MD1  MD2
 SELECT  n1 + 13                               ---  ---  ---  ---  ---
        ,n2 - 4                                -31   11   -2   -9   -9
 FROM    temp1                                 -18    7   -2   -4   -4
 WHERE   n1 < 60                                -5    3   -1   -2   -2
)                                                8   -1   -8    0    0
SELECT   n1                                     21   -5   -4    1    1
        ,n2                                     34   -9   -3    7    7
        ,n1/n2           AS div                 47  -13   -3    8    8
        ,n1-((n1/n2)*n2) AS md1                 60  -17   -3    9    9
        ,MOD(n1,n2)      AS md2
FROM     temp1
ORDER BY 1;
Figure 422, MOD function example






SELECT   dt1                                   ANSWER
        ,MONTH(dt1)                            =======================
        ,MONTHNAME(dt1)                        DT1         2   3
FROM     scalar                                ----------  --  -------
ORDER BY dt1;                                  0001-01-01   1  January
                                               1996-04-22   4  April
                                               1996-08-15   8  August
Figure 423, MONTH and MONTHNAME functions example






WITH temp1 (n1,n2) AS
(VALUES (DECIMAL(1234,10)                                      ANSWER
        ,DECIMAL(1234,10)))                                   ========
SELECT  n1                                               >>      1234.
       ,n2                                               >>      1234.
       ,n1 * n2             AS p1                        >>   1522756.
       ,"*"(n1,n2)          AS p2                        >>   1522756.
       ,MULTIPLY_ALT(n1,n2) AS p3                        >>   1522756.
FROM    temp1;
Figure 424, Multiplying numbers - examples






                                                     <--MULTIPLY_ALT->
                          RESULT        RESULT        SCALE   PRECSION
 INPUT#1     INPUT#2    "*" OPERATOR  MULTIPLY_ALT   TRUNCATD TRUNCATD
==========  ==========  ============  ============   ========  =======
DEC(05,00)  DEC(05,00)  DEC(10,00)    DEC(10,00)           NO       NO
DEC(10,05)  DEC(11,03)  DEC(21,08)    DEC(21,08)           NO       NO
DEC(20,15)  DEC(21,13)  DEC(31,28)    DEC(31,18)          YES       NO
DEC(26,23)  DEC(10,01)  DEC(31,24)    DEC(31,19)          YES       NO
DEC(31,03)  DEC(15,08)  DEC(31,11)    DEC(31,03)          YES      YES
Figure 425, Decimal multiplication - same output lengths






                                                                ANSWER
                                           ===========================
                                           D1                   D2
WITH temp1 (d1) AS                         -------------------- ------
  (VALUES (DECFLOAT(1))                                       1      1
         ,(DECFLOAT(1.0))                                   1.0      1
         ,(DECFLOAT(1.00))                                 1.00      1
         ,(DECFLOAT(1.000))                               1.000      1
         ,(DECFLOAT('12.3'))                               12.3   12.3
         ,(DECFLOAT('12.30'))                             12.30   12.3
         ,(DECFLOAT(1.2e4))                               12000 1.2E+4
         ,(DECFLOAT('1.2e4'))                            1.2E+4 1.2E+4
         ,(DECFLOAT(1.2e-3))               0.001200000000000000 0.0012
         ,(DECFLOAT('1.2e-3'))                           0.0012 0.0012
  )    
SELECT   d1
        ,NORMALIZE_DECFLOAT(d1) AS d2
FROM     temp1;
Figure 426, NORMALIZE_DECFLOAT function examples






SELECT s1                                        ANSWER
      ,NULLIF(s1,0)                              =====================
      ,c1                                        S1  2   C1     4
      ,NULLIF(c1,'AB')                           --- --- ------ ------
FROM   scalar                                     -2  -2 ABCDEF ABCDEF
WHERE  NULLIF(0,0) IS NULL;                        0   - ABCD   ABCD
                                                   1   1 AB     -
Figure 427, NULLIF function examples






WITH temp1 (c1) AS (VALUES (CAST('аи? AS VARCHAR(10))))
SELECT   c1                          AS C1
        ,LENGTH(c1)                  AS LEN
        ,OCTET_LENGTH(c1)            AS OCT                     ANSWER
        ,CHAR_LENGTH(c1,OCTETS)      AS L08    =======================
        ,CHAR_LENGTH(c1,CODEUNITS16) AS L16    C1  LEN OCT L08 L16 L32
        ,CHAR_LENGTH(c1,CODEUNITS32) AS L32    --- --- --- --- --- ---
FROM     temp1;                                аи?  6   6   6   3   3
Figure 428, OCTET_LENGTH example






Figure 429, OVERLAY function syntax






WITH temp1 (txt) AS
   (VALUES('abcded'),('addd'),('adq'))
SELECT   txt
        ,OVERLAY(txt,'XX',3,1,OCTETS)  AS "s3f1"
        ,OVERLAY(txt,'XX',2,  OCTETS)  AS "s2f0"
        ,OVERLAY(txt,'XX',1,1,OCTETS)  AS "s1f1"
        ,OVERLAY(txt,'XX',2,2,OCTETS)  AS "s2f2"
FROM     temp1;                                                 ANSWER
                            ==========================================
                            TXT    s3f1     s2f0     s1f1     s2f2
                            ------ -------- -------- -------- --------
                            abcded abXXded  aXXcded  XXbcded  aXXded
                            addd   adXXd    aXXdd    XXddd    aXXd
                            adq    adXX     aXXq     XXdq     aXX
Figure 430, OVERLAY function example






SELECT   PARTITION(id) AS pp                                    ANSWER
FROM     staff                                                  ======
WHERE    id = 10;                                               PP
                                                                --
                                                                 0
Figure 431, PARTITION function example






Figure 432, POSITION function syntax






WITH temp1 (c1) As                                              ANSWER
   (VALUES ('?),('a?),('ад'),('аа?))                ===============
SELECT   c1                                            C1  p1 p2 p3 p4
        ,POSITION('?,c1,OCTETS)          AS "p1"      --- -- -- -- --
        ,POSITION('?,c1,CODEUNITS16)     AS "p2"      ?   1  1  1  1
        ,POSITION('?,c1,CODEUNITS32)     AS "p3"      a?  2  2  2  2
        ,POSITION('? IN c1 USING OCTETS) AS "p4"      ад   3  2  2  3
FROM     temp1;                                        аа? 5  3  3  5
Figure 433, POSITION function syntax






SELECT   c1                                         ANSWER
        ,POSSTR(c1,' ')  AS p1                      ==================
        ,POSSTR(c1,'CD') AS p2                      C1      P1  P2  P3
        ,POSSTR(c1,'cd') AS p3                      ------  --  --  --
FROM     scalar                                     AB       3   0   0
ORDER BY 1;                                         ABCD     5   3   0
                                                    ABCDEF   0   3   0
Figure 434, POSSTR function example






SELECT c1                                  ANSWER
      ,POSSTR(c1,' ')   AS p1              ===========================
      ,LOCATE(' ',c1)   AS l1              C1     P1 L1 P2 L2 P3 L3 L4
      ,POSSTR(c1,'CD')  AS p2              ------ -- -- -- -- -- -- --
      ,LOCATE('CD',c1)  AS l2              AB      3  3  0  0  0  0  0
      ,POSSTR(c1,'cd')  AS p3              ABCD    5  5  3  3  0  0  4
      ,LOCATE('cd',c1)  AS l3              ABCDEF  0  0  3  3  0  0  4
      ,LOCATE('D',c1,2) AS l4
FROM   scalar
ORDER BY 1;
Figure 435, POSSTR vs. LOCATE functions






WITH temp1(n1) AS                      ANSWER
(VALUES (1),(10),(100))                ===============================
SELECT n1                              N1      P1      P2      P3
      ,POWER(n1,1) AS p1               ------- ------- ------- -------
      ,POWER(n1,2) AS p2                     1       1       1       1
      ,POWER(n1,3) AS p3                    10      10     100    1000
FROM   temp1;                              100     100   10000 1000000
Figure 436, POWER function examples






                                                                ANSWER
WITH temp1 (d1, d2) AS                        ------------------------
  (VALUES (+1.23,    DECFLOAT(1.0))                                1.2
         ,(+1.23,    DECFLOAT(1.00))                              1.23
         ,(-1.23,    DECFLOAT(1.000))                           -1.230
         ,(+123,     DECFLOAT(9.8765))                        123.0000
         ,(+123,     DECFLOAT(1E-3))                           123.000
         ,(+123,     DECFLOAT(1E+3))                               123
         ,(SQRT(2),  DECFLOAT(0.0))                                1.4
         ,(SQRT(2),  DECFLOAT('1E-5'))                         1.41421
         ,(SQRT(2),  DECFLOAT( 1E-5 ))         1.414213562373095100000
  )    
SELECT   QUANTIZE(d1,d2)
FROM     temp1;
Figure 437, QUANTIZE function examples






                                                                ANSWER
WITH temp1 (d1) AS                             -----------------------
  (VALUES (DECFLOAT('1E-5'))                                   0.00001
         ,(DECFLOAT( 1E-5 ))                   0.000010000000000000001
  )    
SELECT   d1
FROM     temp1;
Figure 438, DECFLOAT conversion example






Figure 439, RAISE_ERROR function syntax






SELECT s1                                               ANSWER
      ,CASE                                             ==============
        WHEN s1 < 1 THEN s1                             S1      S2
        ELSE RAISE_ERROR('80001',c1)                    ------  ------
       END AS s2                                            -2      -2
FROM   scalar;                                               0       0
                                                        SQLSTATE=80001
Figure 440, RAISE_ERROR function example






WITH temp (num, ran) AS
(VALUES (INT(1)
        ,RAND(2))
 UNION ALL
 SELECT  num + 1
        ,RAND()
 FROM    temp
 WHERE   num < 100000                                    ANSWER
)                                                        =============
SELECT  COUNT(*)               AS #rows            ==>   100000
       ,COUNT(DISTINCT ran)    AS #values          ==>    31242
       ,DEC(AVG(ran),7,6)      AS avg_ran          ==>        0.499838
       ,DEC(STDDEV(ran),7,6)   AS std_dev                     0.288706
       ,DEC(MIN(ran),7,6)      AS min_ran                     0.000000
       ,DEC(MAX(ran),7,6)      AS max_ran                     1.000000
       ,DEC(MAX(ran),7,6) -
        DEC(MIN(ran),7,6)      AS range                       1.000000
       ,DEC(VAR(ran),7,6)      AS variance                    0.083351
FROM    temp;
Figure 441, Sample output from RAND function






SELECT   deptno  AS dno                    ANSWER
        ,RAND(0) AS ran                    ===========================
FROM     department                        DNO  RAN
WHERE    deptno < 'E'                      ---  ----------------------
ORDER BY 1;                                A00  +1.15970336008789E-003
                                           B01  +2.35572374645222E-001
                                           C01  +6.48152104251228E-001
                                           D01  +7.43736075930052E-002
                                           D11  +2.70241401409955E-001
                                           D21  +3.60026856288339E-001
Figure 442, Make reproducible random numbers (use seed)






SELECT   deptno  AS dno                    ANSWER
        ,RAND()  AS ran                    ===========================
FROM     department                        DNO  RAN
WHERE    deptno < 'D'                      ---  ----------------------
ORDER BY 1;                                A00  +2.55287331766717E-001
                                           B01  +9.85290078432569E-001
                                           C01  +3.18918424024171E-001
Figure 443, Make non-reproducible random numbers (no seed)






WITH Temp1 (col1, col2, col3) AS                   ANSWER
(VALUES (0                                         ===================
        ,SMALLINT(RAND(2)*35)*10                   COL1  COL2  COL3
        ,DECIMAL(RAND()*10000,7,2))                ----  ----  -------
 UNION ALL                                            0     0  9342.32
 SELECT  col1 + 1                                     1   250  8916.28
        ,SMALLINT(RAND()*35)*10                       2   310  5430.76
        ,DECIMAL(RAND()*10000,7,2)                    3   150  5996.88
 FROM   temp1                                         4   110  8066.34
 WHERE  col1 + 1 < 10                                 5    50  5589.77
)                                                     6   130  8602.86
SELECT *                                              7   340   184.94
FROM   temp1;                                         8   310  5441.14
                                                      9    70  9267.55
Figure 444, Use RAND to make sample data






WITH temp1 (col1,ran1,ran2) AS                     ANSWER
(VALUES (0                                         ===================
        ,RAND(2)                                   COL#1  RAN#1  RAN#2
        ,RAND()+(RAND()/1E5) )                     -----  -----  -----
 UNION ALL                                         30000  19698  29998
 SELECT col1 + 1
       ,RAND()
       ,RAND() +(RAND()/1E5)
 FROM   temp1
 WHERE  col1 + 1 < 30000
)      
SELECT COUNT(*)             AS col#1
      ,COUNT(DISTINCT ran1) AS ran#1
      ,COUNT(DISTINCT ran2) AS ran#2
FROM   temp1;
Figure 445, Use RAND to make many distinct random values






SELECT   id                                               ANSWER
        ,name                                             ============
FROM     staff                                            ID  NAME
WHERE    RAND() < 0.1                                     --- --------
ORDER BY id;                                              140 Fraye
                                                          190 Sneider
                                                          290 Quill
Figure 446, Randomly select 10% of matching rows






SELECT   id                                               ANSWER
        ,name                                             ============
FROM    (SELECT   s2.*                                    ID  NAME
                 ,ROW_NUMBER() OVER(ORDER BY r1) AS r2    --- --------
         FROM    (SELECT   s1.*                            10 Sanders
                          ,RAND() AS r1                    30 Marenghi
                  FROM     staff s1                        40 O'Brien
                  WHERE    id <= 100                       70 Rothman
                 )AS s2                                   100 Plotz
        )as s3
WHERE    r2 <= 5
ORDER BY id;
Figure 447, Select five random rows






UPDATE  staff
SET     salary = RAND()*10000
WHERE   id < 50;
Figure 448, Use RAND to assign random salaries






                                      ANSWERS
                                      ================================
SELECT  n1          AS dec        =>  1234567890.123456789012345678901
       ,DOUBLE(n1)  AS dbl        =>             1.23456789012346e+009
       ,REAL(n1)    AS rel        =>                     1.234568e+009
       ,INTEGER(n1) AS int        =>                        1234567890
       ,BIGINT(n1)  AS big        =>                        1234567890
FROM   (SELECT 1234567890.123456789012345678901 AS n1
        FROM   staff
        WHERE  id = 10) AS xxx;
Figure 449, REAL and other numeric function examples






Figure 450, REPEAT function syntax






SELECT   id                                ANSWER
        ,CHAR(REPEAT(name,3),40)           ===========================
FROM     staff                             ID 2
WHERE    id < 40                           -- ------------------------
ORDER BY id;                               10 SandersSandersSanders
                                           20 PernalPernalPernal
                                           30 MarenghiMarenghiMarenghi
Figure 451, REPEAT function example






Figure 452, REPLACE function syntax






SELECT c1                                       ANSWER
      ,REPLACE(c1,'AB','XY') AS r1              ======================
      ,REPLACE(c1,'BA','XY') AS r2              C1      R1      R2
FROM   scalar;                                  ------  ------  ------
                                                ABCDEF  XYCDEF  ABCDEF
                                                ABCD    XYCD    ABCD
                                                AB      XY      AB
Figure 453, REPLACE function examples






SELECT c1                                               ANSWER
      ,REPLACE(REPLACE(                                 ==============
       REPLACE(REPLACE(c1,                              C1      R1
        'AB','XY'),'ab','XY'),                          ------  ------
        'Ab','XY'),'aB','XY')                           ABCDEF  XYCDEF
FROM   scalar;                                          ABCD    XYCD
                                                        AB      XY
Figure 454, Nested REPLACE functions






SELECT   id                                                     ANSWER
        ,salary                                  =====================
        ,RID(staff) AS staff_rid                 ID SALARY   STAFF_RID
FROM     staff                                   -- -------- ---------
WHERE    id < 40                                 10 98357.50 100663300
ORDER BY id;                                     20 78171.25 100663301
                                                 30 77506.75 100663302
Figure 455, RID function example






Figure 456, RID_BIT function example ?single table






Figure 457, RID_BIT function example ?multiple tables






Figure 458, RID_BIT function example ?select row to update






Figure 459, RID_BIT function example ?update row






WITH temp1(c1) AS                                     ANSWER
(VALUES ('  ABC')                                     ================
       ,(' ABC ')                                     C1     C2     L2
       ,('ABC  '))                                    -----  -----  --
SELECT c1                                               ABC   ABC    4
      ,RIGHT(c1,4)         AS c2                       ABC   ABC     4
      ,LENGTH(RIGHT(c1,4)) as l2                      ABC    BC      4
FROM   temp1;
Figure 460, RIGHT function examples






                       ANSWER
                       ===============================================
                       D1      P2      P1      P0      N1      N2
                       ------- ------- ------- ------- ------- -------
WITH temp1(d1) AS      123.400 123.400 123.400 123.000 120.000 100.000
(VALUES (123.400)       23.450  23.450  23.400  23.000  20.000   0.000
       ,( 23.450)        3.456   3.460   3.500   3.000   0.000   0.000
       ,(  3.456)        0.056   0.060   0.100   0.000   0.000   0.000
       ,(   .056))
SELECT d1
      ,DEC(ROUND(d1,+2),6,3) AS p2
      ,DEC(ROUND(d1,+1),6,3) AS p1
      ,DEC(ROUND(d1,+0),6,3) AS p0
      ,DEC(ROUND(d1,-1),6,3) AS n1
      ,DEC(ROUND(d1,-2),6,3) AS n2
FROM   temp1;
Figure 461, ROUND function examples






SELECT c1                                       ANSWER
      ,RTRIM(c1)         AS r1                  ======================
      ,LENGTH(c1)        AS r2                  C1      R1      R2  R3
      ,LENGTH(RTRIM(c1)) AS r3                  ------  ------  --  --
FROM   scalar;                                  ABCDEF  ABCDEF   6   6
                                                ABCD    ABCD     6   4
                                                AB      AB       6   2
Figure 462, RTRIM function example






SELECT d1                    ANSWER  (float output shortened)
      ,SIGN(d1)              =========================================
      ,f1                    D1     2           F1          4
      ,SIGN(f1)              -----  ----------  ----------  ----------
FROM   scalar;                -2.4   -1.000E+0   -2.400E+0   -1.000E+0
                               0.0   +0.000E+0   +0.000E+0   +0.000E+0
                               1.8   +1.000E+0   +1.800E+0   +1.000E+0
Figure 463, SIGN function examples






WITH temp1(n1) AS                              ANSWER
(VALUES (0)                                    =======================
 UNION ALL                                     N1  RAN    SIN    TAN
 SELECT  n1 + 10                               --  -----  -----  -----
 FROM    temp1                                  0  0.000  0.000  0.000
 WHERE   n1 < 80)                              10  0.174  0.173  0.176
SELECT n1                                      20  0.349  0.342  0.363
      ,DEC(RADIANS(n1),4,3)      AS ran        30  0.523  0.500  0.577
      ,DEC(SIN(RADIANS(n1)),4,3) AS sin        40  0.698  0.642  0.839
      ,DEC(TAN(RADIANS(n1)),4,3) AS tan        50  0.872  0.766  1.191
FROM   temp1;                                  60  1.047  0.866  1.732
                                               70  1.221  0.939  2.747
                                               80  1.396  0.984  5.671
Figure 464, SIN function example






SELECT d1                           ANSWER
      ,SMALLINT(d1)                 ==================================
      ,SMALLINT('+123')             D1    2      3      4      5
      ,SMALLINT('-123')             ----- ------ ------ ------ ------
      ,SMALLINT(' 123 ')             -2.4     -2    123   -123    123
FROM   scalar;                        0.0      0    123   -123    123
                                      1.8      1    123   -123    123
Figure 465, SMALLINT function examples






SELECT   a.name          AS n1          ANSWER
        ,SOUNDEX(a.name) AS s1          ==============================
        ,b.name          AS n2          N1      S1   N2        S2   DF
        ,SOUNDEX(b.name) AS s2          ------- ---- --------- ---- --
        ,DIFFERENCE                     Sanders S536 Sneider   S536  4
         (a.name,b.name) AS df          Sanders S536 Smith     S530  3
FROM     staff a                        Sanders S536 Lundquist L532  2
        ,staff b                        Sanders S536 Daniels   D542  1
WHERE    a.id = 10                      Sanders S536 Molinare  M456  1
  AND    b.id > 150                     Sanders S536 Scoutten  S350  1
  AND    b.id < 250                     Sanders S536 Abrahams  A165  0
ORDER BY df DESC                        Sanders S536 Kermisch  K652  0
        ,n2 ASC;                        Sanders S536 Lu        L000  0
Figure 466, SOUNDEX function example






WITH temp1(n1) AS                                   ANSWER
(VALUES (1),(2),(3))                                ==================
SELECT n1                                           N1  S1    S2  S3
      ,SPACE(n1)         AS s1                      --  ----  --  ----
      ,LENGTH(SPACE(n1)) AS s2                       1         1   X
      ,SPACE(n1) || 'X'  AS s3                       2         2    X
FROM   temp1;                                        3         3     X
Figure 467, SPACE function examples






WITH temp1(n1) AS                                         ANSWER
(VALUES (0.5),(0.0)                                       ============
       ,(1.0),(2.0))                                      N1     S1
SELECT DEC(n1,4,3)       AS n1                            -----  -----
      ,DEC(SQRT(n1),4,3) AS s1                            0.500  0.707
FROM   temp1;                                             0.000  0.000
                                                          1.000  1.000
                                                          2.000  1.414
Figure 468, SQRT function example






Figure 469, STRIP function syntax






WITH temp1(c1) AS                                               ANSWER
(VALUES ('  ABC')                        =============================
       ,(' ABC ')                        C1    C2    L2 C3    L3 C4
       ,('ABC  '))                       ----- ----- -- ----- -- -----
                                           ABC ABC    3 ABC    3   ABC
SELECT c1                        AS C1    ABC  ABC    3 ABC    4  ABC
      ,STRIP(c1)                 AS C2   ABC   ABC    3 ABC    5 BC
      ,LENGTH(STRIP(c1))         AS L2
      ,STRIP(c1,LEADING)         AS C3
      ,LENGTH(STRIP(c1,LEADING)) AS L3
      ,STRIP(c1,LEADING,'A')     AS C4
FROM   temp1;
Figure 470, STRIP function example






Figure 471, SUBSTR function syntax






WITH temp1 (len, dat1) AS                    ANSWER
(VALUES    (  6,'123456789')                 =========================
          ,(  4,'12345'    )                 LEN DAT1      LDAT SUBDAT
          ,( 16,'123'      )                 --- --------- ---- ------
)                                              6 123456789    9 123456
SELECT    len                                  4 12345        5 1234
         ,dat1                               
         ,LENGTH(dat1)       AS ldat
         ,SUBSTR(dat1,1,len) AS subdat
FROM      temp1;
Figure 472, SUBSTR function - error because length parm too long






WITH temp1 (len, dat1) AS                    ANSWER
(VALUES    (  6,'123456789')                 =========================
          ,(  4,'12345'    )                 LEN DAT1      LDAT SUBDAT
          ,( 16,'123'      )                 --- --------- ---- ------
)                                              6 123456789    9 123456
SELECT    len                                  4 12345        5 1234
         ,dat1                                16 123          3 123
         ,LENGTH(dat1)  AS ldat
         ,SUBSTR(dat1,1,CASE
                        WHEN len < LENGTH(dat1) THEN len
                        ELSE LENGTH(dat1)
                        END ) AS subdat
FROM      temp1;
Figure 473, SUBSTR function - avoid error using CASE (see previous)






SELECT name                                ANSWER
      ,LENGTH(name)             AS len     ===========================
      ,SUBSTR(name,5)           AS s1      NAME     LEN S1   L1 S2  L2
      ,LENGTH(SUBSTR(name,5))   AS l1      -------- --- ---- -- --- --
      ,SUBSTR(name,5,3)         AS s2      Sanders    7 ers   3 ers  3
      ,LENGTH(SUBSTR(name,5,3)) AS l2      Pernal     6 al    2 al   3
FROM   staff                               Marenghi   8 nghi  4 ngh  3
WHERE  id < 60;                            O'Brien    7 ien   3 ien  3
                                           Hanes      5 s     1 s    3
Figure 474, SUBSTR function - fixed length output if third parm. used






SELECT   a.id                               ANSWER
        ,a.dept                             ==========================
        ,a.salary                           ID DEPT SALARY   DEPTSAL
        ,b.deptsal                          -- ---- -------- ---------
FROM     staff  a                           10 20   98357.50 254286.10
        ,TABLE                              20 20   78171.25 254286.10
        (SELECT   b.dept                    30 38   77506.75 302285.55
                 ,SUM(b.salary) AS deptsal
         FROM     staff b
         WHERE    b.dept = a.dept
         GROUP BY b.dept
        )AS b
WHERE    a.id   < 40
ORDER BY a.id;
Figure 475, Fullselect with external table reference






CREATE ALIAS emp1 FOR employee;                ANSWER
CREATE ALIAS emp2 FOR emp1;                    =======================
                                               TABSCHEMA TABNAME  CARD
SELECT tabschema                               --------- -------- ----
      ,tabname                                 graeme    employee   -1
      ,card
FROM   syscat.tables
WHERE  tabname   = TABLE_NAME('emp2','graeme');
Figure 476, TABLE_NAME function example






CREATE VIEW fred1 (c1, c2, c3)             ANSWER
AS VALUES (11, 'AAA', 'BBB');              ===========================
                                           TAB_SCH  TAB_NME
CREATE ALIAS fred2 FOR fred1;              -------- ------------------
CREATE ALIAS fred3 FOR fred2;              graeme   fred1
                                           graeme   xxxxx
DROP VIEW fred1;
       
WITH temp1 (tab_sch, tab_nme) AS
(VALUES (TABLE_SCHEMA('fred3','graeme'),TABLE_NAME('fred3')),
        (TABLE_SCHEMA('xxxxx')         ,TABLE_NAME('xxxxx','xxx')))
SELECT *
FROM   temp1;
Figure 477, TABLE_SCHEMA and TABLE_NAME functions example






SELECT TIMESTAMP('1997-01-11-22.44.55.000000')
      ,TIMESTAMP('1997-01-11-22.44.55.000')
      ,TIMESTAMP('1997-01-11-22.44.55')
      ,TIMESTAMP('19970111224455')
      ,TIMESTAMP('1997-01-11','22.44.55')
FROM   staff
WHERE  id = 10;
Figure 478, TIMESTAMP function examples






WITH temp1 (ts1) AS
(VALUES  ('1999-12-31 23:59:59')
        ,('2002-10-30 11:22:33')
)      
SELECT   ts1
        ,TIMESTAMP_FORMAT(ts1,'YYYY-MM-DD HH24:MI:SS') AS ts2
FROM     temp1
ORDER BY ts1;                                                   ANSWER
                       ===============================================
                       TS1                  TS2
                       -------------------  --------------------------
                       1999-12-31 23:59:59  1999-12-31-23.59.59.000000
                       2002-10-30 11:22:33  2002-10-30-11.22.33.000000
Figure 479, TIMESTAMP_FORMAT function example






SELECT tm1                         ANSWER
      ,TIMESTAMP_ISO(tm1)          ===================================
FROM   scalar;                     TM1      2
                                   -------- --------------------------
                                   23:58:58 2000-09-01-23.58.58.000000
                                   15:15:15 2000-09-01-15.15.15.000000
                                   00:00:00 2000-09-01-00.00.00.000000
Figure 480, TIMESTAMP_ISO function example






WITH   
temp1 (ts1,ts2) AS
  (VALUES ('1996-03-01-00.00.01','1995-03-01-00.00.00')
         ,('1996-03-01-00.00.00','1995-03-01-00.00.01')),
temp2 (ts1,ts2) AS
  (SELECT  TIMESTAMP(ts1)
          ,TIMESTAMP(ts2)
   FROM    temp1),
temp3 (ts1,ts2,df) AS
  (SELECT  ts1
          ,ts2
          ,CHAR(TS1 - TS2) AS df         ANSWER
   FROM    temp2)                        =============================
SELECT df                                DF                    DIF DYS
      ,TIMESTAMPDIFF(16,df)  AS dif      --------------------- --- ---
      ,DAYS(ts1) - DAYS(ts2) AS dys      00010000000001.000000 365 366
FROM   temp3;                            00001130235959.000000 360 366
Figure 481, TIMESTAMPDIFF function example






CREATE FUNCTION ts_diff_works(in_hi TIMESTAMP,in_lo TIMESTAMP)
RETURNS BIGINT
RETURN (BIGINT(DAYS(in_hi))              * 86400000000
      + BIGINT(MIDNIGHT_SECONDS(in_hi))  *     1000000
      + BIGINT(MICROSECOND(in_hi)))
      -(BIGINT(DAYS(in_lo))              * 86400000000
      + BIGINT(MIDNIGHT_SECONDS(in_lo))  *     1000000
      + BIGINT(MICROSECOND(in_lo)));
Figure 482, Function to get difference between two timestamps






                                                                ANSWER
WITH temp1 (d1, d2) AS                                          ======
  (VALUES (DECFLOAT(+1.0), DECFLOAT(+1.0))                           0
         ,(DECFLOAT(+1.0), DECFLOAT(+1.00))                          1
         ,(DECFLOAT(-1.0), DECFLOAT(-1.00))                         -1
         ,(DECFLOAT(+0.0), DECFLOAT(+0.00))                          1
         ,(DECFLOAT(-0.0), DECFLOAT(-0.00))                          1
         ,(DECFLOAT(1234), +infinity)                               -1
         ,(+infinity,      +infinity)                                0
         ,(+infinity,      -infinity)                                1
         ,(DECFLOAT(1234), -NaN)                                     1
  )    
SELECT   TOTALORDER(d1,d2)
FROM     temp1;
Figure 483, TOTALORDER function example






Figure 484, TRANSLATE function syntax






                                                ANS. NOTES
                                                ==== =================
SELECT 'abcd'                              ==>  abcd No change
      ,TRANSLATE('abcd')                   ==>  ABCD Make upper case
      ,TRANSLATE('abcd','','a')            ==>   bcd 'a'=>' '
      ,TRANSLATE('abcd','A','A')                abcd 'A'=>'A'
      ,TRANSLATE('abcd','A','a')                Abcd 'a'=>'A'
      ,TRANSLATE('abcd','A','ab')               A cd 'a'=>'A','b'=>' '
      ,TRANSLATE('abcd','A','ab',' ')           A cd 'a'=>'A','b'=>' '
      ,TRANSLATE('abcd','A','ab','z')           Azcd 'a'=>'A','b'=>'z'
      ,TRANSLATE('abcd','AB','a')               Abcd 'a'=>'A'
FROM   staff
WHERE  id = 10;
Figure 485, TRANSLATE function examples






                                                                ANSWER
                                                                ======
SELECT c1                                                  ==>  ABCD
      ,REPLACE(c1,'AB','XY')                               ==>  XYCD
      ,REPLACE(c1,'BA','XY')                               ==>  ABCD
      ,TRANSLATE(c1,'XY','AB')                                  XYCD
      ,TRANSLATE(c1,'XY','BA')                                  YXCD
FROM   scalar
WHERE  c1 = 'ABCD';
Figure 486, REPLACE vs. TRANSLATE






                       ANSWER
                       ===============================================
                       D1      POS2    POS1    ZERO    NEG1    NEG2
                       ------- ------- ------- ------- ------- -------
WITH temp1(d1) AS      123.400 123.400 123.400 123.000 120.000 100.000
(VALUES (123.400)       23.450  23.440  23.400  23.000  20.000   0.000
       ,( 23.450)        3.456   3.450   3.400   3.000   0.000   0.000
       ,(  3.456)        0.056   0.050   0.000   0.000   0.000   0.000
       ,(   .056))
SELECT d1
      ,DEC(TRUNC(d1,+2),6,3) AS pos2
      ,DEC(TRUNC(d1,+1),6,3) AS pos1
      ,DEC(TRUNC(d1,+0),6,3) AS zero
      ,DEC(TRUNC(d1,-1),6,3) AS neg1
      ,DEC(TRUNC(d1,-2),6,3) AS neg2
FROM   temp1
ORDER BY 1 DESC;
Figure 487, TRUNCATE function examples






SELECT name                                  ANSWER
      ,LCASE(name) AS lname                  =========================
      ,UCASE(name) AS uname                  NAME     LNAME    UNAME
FROM   staff                                 -------  -------  -------
WHERE  id < 30;                              Sanders  sanders  SANDERS
                                             Pernal   pernal   PERNAL
Figure 488, UCASE function example






SELECT c1                                     ANSWER
      ,LENGTH(c1)          AS l1              ========================
      ,VARCHAR(c1)         AS v2              C1     L1 V2     L2 V3
      ,LENGTH(VARCHAR(c1)) AS l2              ------ -- ------ -- ----
      ,VARCHAR(c1,4)       AS v3              ABCDEF  6 ABCDEF  6 ABCD
FROM   scalar;                                ABCD    6 ABCD    6 ABCD
                                              AB      6 AB      6 AB
Figure 489, VARCHAR function examples






WITH temp1 (ts1) AS
(VALUES  (TIMESTAMP('1999-12-31-23.59.59'))
        ,(TIMESTAMP('2002-10-30-11.22.33'))
)      
SELECT   ts1
        ,VARCHAR_FORMAT(ts1,'YYYY-MM-DD HH24:MI:SS') AS ts2
FROM     temp1
ORDER BY ts1;                                                   ANSWER
                        ==============================================
                        TS1                        TS2
                        -------------------------- -------------------
                        1999-12-31-23.59.59.000000 1999-12-31 23:59:59
                        2002-10-30-11.22.33.000000 2002-10-30 11:22:33
Figure 490, VARCHAR_FORMAT function example






SELECT  WEEK(DATE('2000-01-01')) AS w1              ANSWER
       ,WEEK(DATE('2000-01-02')) AS w2              ==================
       ,WEEK(DATE('2001-01-02')) AS w3              W1  W2  W3  W4  W5
       ,WEEK(DATE('2000-12-31')) AS w4              --  --  --  --  --
       ,WEEK(DATE('2040-12-31')) AS w5               1   2   1  54  53
FROM    sysibm.sysdummy1;
Figure 491, WEEK function examples






WITH                                        ANSWER
temp1 (n) AS                                ==========================
  (VALUES (0)                               DTE        DY  WK DY WI DI
   UNION ALL                                ---------- --- -- -- -- --
   SELECT n+1                               1998-12-27 Sun 53  1 52  7
   FROM   temp1                             1998-12-28 Mon 53  2 53  1
   WHERE  n < 10),                          1998-12-29 Tue 53  3 53  2
temp2 (dt2) AS                              1998-12-30 Wed 53  4 53  3
  (SELECT DATE('1998-12-27') + y.n YEARS    1998-12-31 Thu 53  5 53  4
                             + d.n DAYS     1999-01-01 Fri  1  6 53  5
   FROM   temp1 y                           1999-01-02 Sat  1  7 53  6
         ,temp1 d                           1999-01-03 Sun  2  1 53  7
   WHERE  y.n IN (0,2))                     1999-01-04 Mon  2  2  1  1
SELECT   CHAR(dt2,ISO)              dte     1999-01-05 Tue  2  3  1  2
        ,SUBSTR(DAYNAME(dt2),1,3)   dy      1999-01-06 Wed  2  4  1  3
        ,WEEK(dt2)                  wk      2000-12-27 Wed 53  4 52  3
        ,DAYOFWEEK(dt2)             dy      2000-12-28 Thu 53  5 52  4
        ,WEEK_ISO(dt2)              wi      2000-12-29 Fri 53  6 52  5
        ,DAYOFWEEK_ISO(dt2)         di      2000-12-30 Sat 53  7 52  6
FROM     temp2                              2000-12-31 Sun 54  1 52  7
ORDER BY 1;                                 2001-01-01 Mon  1  2  1  1
                                            2001-01-02 Tue  1  3  1  2
                                            2001-01-03 Wed  1  4  1  3
                                            2001-01-04 Thu  1  5  1  4
                                            2001-01-05 Fri  1  6  1  5
                                            2001-01-06 Sat  1  7  1  6
Figure 492, WEEK_ISO function example






SELECT dt1                                      ANSWER
      ,YEAR(dt1) AS yr                          ======================
      ,WEEK(dt1) AS wk                          DT1         YR    WK
FROM   scalar;                                  ----------  ----  ----
                                                1996-04-22  1996    17
                                                1996-08-15  1996    33
                                                0001-01-01     1     1
Figure 493, YEAR and WEEK functions example






SELECT   id                              ANSWER
        ,salary                          =============================
        ,"+"(salary)    AS s2            ID SALARY   S2       S3
        ,"+"(salary,id) AS s3            -- -------- -------- --------
FROM     staff                           10 98357.50 98357.50 98367.50
WHERE    id < 40                         20 78171.25 78171.25 78191.25
ORDER BY id;                             30 77506.75 77506.75 77536.75
Figure 494, PLUS function examples






SELECT   empno
        ,CHAR(birthdate,ISO)                            AS bdate1
        ,CHAR(birthdate + 1 YEAR,ISO)                   AS bdate2
        ,CHAR("+"(birthdate,DEC(00010000,8)),ISO)       AS bdate3
        ,CHAR("+"(birthdate,DOUBLE(1),SMALLINT(1)),ISO) AS bdate4
FROM     employee
WHERE    empno < '000040'
ORDER BY empno;                                                 ANSWER
                    ==================================================
                    EMPNO  BDATE1     BDATE2     BDATE3     BDATE4
                    ------ ---------- ---------- ---------- ----------
                    000010 1933-08-24 1934-08-24 1934-08-24 1934-08-24
                    000020 1948-02-02 1949-02-02 1949-02-02 1949-02-02
                    000030 1941-05-11 1942-05-11 1942-05-11 1942-05-11
Figure 495, Adding one year to date value






SELECT   id                             ANSWER
        ,salary                         ==============================
        ,"-"(salary)    AS s2           ID SALARY   S2        S3
        ,"-"(salary,id) AS s3           -- -------- --------- --------
FROM     staff                          10 98357.50 -98357.50 98347.50
WHERE    id < 40                        20 78171.25 -78171.25 78151.25
ORDER BY id;                            30 77506.75 -77506.75 77476.75
Figure 496, MINUS function examples






SELECT   id                          ANSWER
        ,salary                      =================================
        ,salary * id    AS s2        ID SALARY   S2         S3
        ,"*"(salary,id) AS s3        -- -------- ---------- ----------
FROM     staff                       10 98357.50  983575.00  983575.00
WHERE    id < 40                     20 78171.25 1563425.00 1563425.00
ORDER BY id;                         30 77506.75 2325202.50 2325202.50
Figure 497, MULTIPLY function examples






SELECT   id                                ANSWER
        ,salary                            ===========================
        ,salary / id    AS s2              ID SALARY   S2       S3
        ,"/"(salary,id) AS s3              -- -------- ------- -------
FROM     staff                             10 98357.50 9835.75 9835.75
WHERE    id < 40                           20 78171.25 3908.56 3908.56
ORDER BY id;                               30 77506.75 2583.55 2583.55
Figure 498, DIVIDE function examples






SELECT   id                                ANSWER
        ,name || 'Z'      AS n1            ===========================
        ,name CONCAT 'Z'  AS n2            ID  N1    N2    N3    N4
        ,"||"(name,'Z')   As n3            --- ----- ----- ----- -----
        ,CONCAT(name,'Z') As n4            110 NganZ NganZ NganZ NganZ
FROM     staff                             210 LuZ   LuZ   LuZ   LuZ
WHERE    LENGTH(name) < 5                  270 LeaZ  LeaZ  LeaZ  LeaZ
ORDER BY id;
Figure 499, CONCAT function examples






Figure 500, Sourced function syntax






CREATE  FUNCTION digi_int (SMALLINT)
RETURNS CHAR(5)
SOURCE  SYSIBM.DIGITS(SMALLINT);
Figure 501, Create sourced function






SELECT   id            AS ID                            ANSWER
        ,DIGITS(id)    AS I2                            ==============
        ,digi_int(id)  AS I3                            ID I2    I3
FROM     staff                                          -- ----- -----
WHERE    id < 40                                        10 00010 00010
ORDER BY id;                                            20 00020 00020
                                                        30 00030 00030
Figure 502, Using sourced function - works






SELECT  id                                                     ANSWER
       ,digi_int(INT(id))                                      =======
FROM    staff                                                  
WHERE   id < 50;
Figure 503, Using sourced function - fails






CREATE DISTINCT TYPE us_dollars AS DEC(7,2) WITH COMPARISONS;
       
CREATE TABLE customers
(ID        SMALLINT     NOT NULL
,balance   us_dollars   NOT NULL);
                                                            ANSWER
INSERT INTO customers VALUES (1 ,111.11),(2 ,222.22);       ==========
                                                            ID balance
SELECT   *                                                  -- -------
FROM     customers                                           1  111.11
ORDER BY ID;                                                 2  222.22
Figure 504, Create distinct type and test table






SELECT   id                                                    ANSWER
        ,balance * 10                                          =======
FROM     customers                                             
ORDER BY id;
Figure 505, Do multiply - fails






CREATE FUNCTION "*" (us_dollars,INT)
RETURNS us_dollars
SOURCE SYSIBM."*"(DECIMAL,INT);
Figure 506, Create sourced function






SELECT   id                                                 ANSWER
        ,balance * 10 AS newbal                             ==========
FROM     customers                                          ID NEWBAL
ORDER BY id;                                                -- -------
                                                             1 1111.10
                                                             2 2222.20
Figure 507, Do multiply - works






SELECT   id                                                 ANSWER
        ,"*"(balance,10) AS newbal                          ==========
FROM     customers                                          ID NEWBAL
ORDER BY id;                                                -- -------
                                                             1 1111.10
                                                             2 2222.20
Figure 508, Do multiply - works






Figure 509, Scalar and Table function syntax






CREATE FUNCTION Test()    RETURNS CHAR(5)    RETURN 'abcde';
Figure 510, Function returns nullable, but never null, value






CREATE FUNCTION returns_zero() RETURNS SMALLINT RETURN 0;
                                                                ANSWER
SELECT   id              AS id                                  ======
        ,returns_zero()  AS zz                                  ID ZZ
FROM     staff                                                  -- --
WHERE    id = 10;                                               10  0
Figure 511, Simple function usage






CREATE FUNCTION calc(inval SMALLINT) RETURNS INT RETURN inval * 10;
CREATE FUNCTION calc(inval INTEGER)  RETURNS INT RETURN inval *  5;
       
SELECT   id                 AS id                           ANSWER
        ,calc(SMALLINT(id)) AS c1                           ==========
        ,calc(INTEGER (id)) AS C2                           ID C1  C2
FROM     staff                                              -- --- ---
WHERE    id < 30                                            10 100  50
ORDER BY id;                                                20 200 100
       
DROP FUNCTION calc(SMALLINT);
DROP FUNCTION calc(INTEGER);
Figure 512, Two functions with same name






CREATE FUNCTION rnd(inval INT)
RETURNS SMALLINT
NOT DETERMINISTIC
RETURN RAND() * 50;                                             ANSWER
                                                                ======
SELECT   id     AS id                                           ID RND
        ,rnd(1) AS RND                                          -- ---
FROM     staff                                                  10  37
WHERE    id < 40                                                20   8
ORDER BY id;                                                    30  42
Figure 513, Not deterministic function






CREATE FUNCTION get_sal(inval SMALLINT)
RETURNS DECIMAL(7,2)
RETURN SELECT salary
       FROM   staff
       WHERE  id = inval;                                  ANSWER
                                                           ===========
SELECT   id          AS id                                 ID SALARY
        ,get_sal(id) AS salary                             -- --------
FROM     staff                                             10 98357.50
WHERE    id < 40                                           20 78171.25
ORDER BY id;                                               30 77506.75
Figure 514, Function using query






CREATE FUNCTION max_sal(inval SMALLINT)
RETURNS DECIMAL(7,2)
RETURN WITH
   ddd (max_sal) AS
   (SELECT  MAX(S2.salary)
    FROM    staff S1
           ,staff S2
    WHERE   S1.id    =  inval
      AND   S1.dept  =  s2.dept)
  ,yyy (max_sal) AS
   (SELECT  MAX(S2.salary)
    FROM    staff S1
           ,staff S2
    WHERE   S1.id    =  inval
      AND   S1.years =  s2.years)
SELECT CASE
          WHEN ddd.max_sal > yyy.max_sal
          THEN ddd.max_sal
          ELSE yyy.max_sal
       END
FROM   ddd, yyy;
                                                  ANSWER
SELECT   id          AS id                        ====================
        ,salary      AS SAL1                      ID SAL1     SAL2
        ,max_sal(id) AS SAL2                      -- -------- --------
FROM     staff                                    10 98357.50 98357.50
WHERE    id < 40                                  20 78171.25 98357.50
ORDER BY id;                                      30 77506.75 79260.25
Figure 515, Function using common table expression






CREATE FUNCTION remove_e(instr VARCHAR(50))
RETURNS VARCHAR(50)
RETURN replace(instr,'e','');
       
UPDATE   staff
SET      name = remove_e(name)
WHERE    id < 40;
Figure 516, Function used in update






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE FUNCTION reverse(instr VARCHAR(50))                This example
RETURNS VARCHAR(50)                                       uses an "!"
BEGIN ATOMIC                                              as the stmt
   DECLARE outstr  VARCHAR(50) DEFAULT '';                delimiter.
   DECLARE curbyte SMALLINT    DEFAULT 0;
   SET curbyte = LENGTH(RTRIM(instr));
   WHILE curbyte >= 1 DO
      SET outstr  = outstr || SUBSTR(instr,curbyte,1);
      SET curbyte = curbyte - 1;
   END WHILE;
   RETURN outstr;
END!   
                                                  ANSWER
SELECT   id             AS id                     ====================
        ,name           AS name1                  ID NAME1    NAME2
        ,reverse(name)  AS name2                  -- -------- -------
FROM     staff                                    10 Sanders  srednaS
WHERE    id < 40                                  20 Pernal   lanreP
ORDER BY id!                                      30 Marenghi ihgneraM
Figure 517, Function using compound SQL






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE FUNCTION check_len(instr VARCHAR(50))              This example
RETURNS SMALLINT                                          uses an "!"
BEGIN ATOMIC                                              as the stmt
   IF instr IS NULL THEN                                  delimiter.
      RETURN NULL;
   END IF;
   IF length(instr) < 6 THEN
      SIGNAL SQLSTATE '75001'
      SET MESSAGE_TEXT = 'Input string is < 6';
   ELSEIF length(instr) < 7 THEN
      RETURN -1;
   END IF;
   RETURN length(instr);                             ANSWER
END!                                                 =================
                                                     ID NAME1    NAME2
SELECT   id              AS id                       -- -------- -----
        ,name            AS name1                    10 Sanders      7
        ,check_len(name) AS name2                    20 Pernal      -1
FROM     staff                                       30 Marenghi     8
WHERE    id < 60                                     40 O'Brien      7
ORDER BY id!                                         
Figure 518, Function with error checking logic






CREATE FUNCTION get_staff()
RETURNS TABLE (ID     SMALLINT
              ,name   VARCHAR(9)
              ,YR     SMALLINT)
RETURN SELECT  id
              ,name
              ,years                                    ANSWER
       FROM    staff;                                   ==============
                                                        ID NAME     YR
SELECT   *                                              -- -------- --
FROM     TABLE(get_staff()) AS s                        10 Sanders   7
WHERE    id < 40                                        20 Pernal    8
ORDER BY id;                                            30 Marenghi  5
Figure 519, Simple table function






Figure 520, Table function usage - syntax






CREATE FUNCTION get_st(inval INTEGER)
RETURNS TABLE (id     SMALLINT
              ,name   VARCHAR(9)
              ,yr     SMALLINT)
RETURN SELECT  id
              ,name
              ,years
       FROM    staff                                    ANSWER
       WHERE   id = inval;                              ==============
                                                        ID NNN      YY
SELECT   *                                              -- -------- --
FROM     TABLE(get_st(30)) AS sss (id, nnn, yy);        30 Marenghi  5
Figure 521, Table function with parameters






CREATE FUNCTION make_data()
RETURNS TABLE (KY   SMALLINT
              ,DAT  CHAR(5))
RETURN  WITH temp1 (k#) AS (VALUES (1),(2),(3))               ANSWER
        SELECT k#                                             ========
              ,DIGITS(SMALLINT(k#))                           KY DAT
        FROM   temp1;                                         -- -----
                                                               1 00001
SELECT   *                                                     2 00002
FROM     TABLE(make_data()) AS ttt;                            3 00003
Figure 522, Table function that creates data






CREATE FUNCTION staff_list(lo_key INTEGER                 IMPORTANT
                          ,lo_sal INTEGER)                ============
RETURNS TABLE (id      SMALLINT                           This example
              ,salary  DECIMAL(7,2)                       uses an "!"
              ,max_sal DECIMAL(7,2)                       as the stmt
              ,id_max  SMALLINT)                          delimiter.
LANGUAGE SQL
READS SQL DATA
EXTERNAL ACTION
DETERMINISTIC
BEGIN ATOMIC
   DECLARE hold_sal DECIMAL(7,2) DEFAULT 0;
   DECLARE hold_key SMALLINT;
   IF lo_sal < 0 THEN
      SIGNAL SQLSTATE '75001'
      SET MESSAGE_TEXT = 'Salary too low';
   END IF;
   FOR get_max AS
      SELECT id     AS in_key
            ,salary As in_sal
      FROM   staff
      WHERE  id >= lo_key
   DO  
      IF in_sal > hold_sal THEN
         SET hold_sal = in_sal;
         SET hold_key = in_key;
      END IF;
   END FOR;
   RETURN
      SELECT id
            ,salary
            ,hold_sal
            ,hold_key                     ANSWER
      FROM   staff                        ============================
      WHERE  id >= lo_key;                ID  SALARY   MAX_SAL  ID_MAX
END!                                      --- -------- -------- ------
                                           70 76502.83 91150.00    140
SELECT   *                                 80 43504.60 91150.00    140
FROM     TABLE(staff_list(66,1)) AS ttt    90 38001.75 91150.00    140
WHERE    id < 111                         100 78352.80 91150.00    140
ORDER BY id!                              110 42508.20 91150.00    140
Figure 523, Table function with compound SQL






CREATE FUNCTION julian_out(inval DATE)
RETURNS CHAR(7)
RETURN  RTRIM(CHAR(YEAR(inval)))
    ||  SUBSTR(DIGITS(DAYOFYEAR(inval)),8);
                                                                ANSWER
SELECT   empno                               =========================
        ,CHAR(hiredate,ISO)   AS h_date      EMPNO  H_DATE     J_DATE
        ,JULIAN_OUT(hiredate) AS j_date      ------ ---------- -------
FROM     employee                            000010 1995-01-01 1995001
WHERE    empno < '000050'                    000020 2003-10-10 2003283
ORDER BY empno;                              000030 2005-04-05 2005095
Figure 524, Convert Date into Julian Date






CREATE FUNCTION julian_in(inval CHAR(7))
RETURNS DATE
RETURN  DATE('0001-01-01')
     +  (INT(SUBSTR(inval,1,4)) - 1) YEARS
     +  (INT(SUBSTR(inval,5,3)) - 1) DAYS;
Figure 525, Convert Julian Date into Date






SELECT   empno
        ,hiredate
FROM     employee
WHERE    YEAR(hiredate) = YEAR(CURRENT DATE) - 1;
       
Figure 526, Select rows where hire-date = prior year






CREATE FUNCTION year_month(inval DATE)
RETURNS INTEGER
RETURN  (YEAR(inval) * 12) + MONTH(inval);
Figure 527, Create year-month function






SELECT   empno
        ,hiredate
FROM     employee
WHERE    YEAR_MONTH(hiredate) = YEAR_MONTH(CURRENT DATE) - 1;
Figure 528, Select rows where hire-date = prior month






CREATE FUNCTION sunday_week(inval DATE)
RETURNS INTEGER
RETURN  DAYS(inval) / 7;
Figure 529, Create week-number function






CREATE FUNCTION monday_week(inval DATE)
RETURNS INTEGER
RETURN  (DAYS(inval) - 1) / 7;
Figure 530, Create week-number function






WITH                                                            ANSWER
temp1 (num,dt) AS                   ==================================
  (VALUES (1                        DATE       DAY WK IS SUN_WK MON_WK
          ,DATE('2004-12-29'))      ---------- --- -- -- ------ ------
   UNION ALL                        2004-12-29 Wed 53 53 104563 104563
   SELECT  num + 1                  2004-12-30 Thu 53 53 104563 104563
          ,dt + 1 DAY               2004-12-31 Fri 53 53 104563 104563
   FROM    temp1                    2005-01-01 Sat  1 53 104563 104563
   WHERE   num < 15                 2005-01-02 Sun  2 53 104564 104563
  ),                                2005-01-03 Mon  2  1 104564 104564
temp2 (dt,dy) AS                    2005-01-04 Tue  2  1 104564 104564
  (SELECT  dt                       2005-01-05 Wed  2  1 104564 104564
          ,SUBSTR(DAYNAME(dt),1,3)  2005-01-06 Thu  2  1 104564 104564
   FROM    temp1                    2005-01-07 Fri  2  1 104564 104564
  )                                 2005-01-08 Sat  2  1 104564 104564
SELECT   CHAR(dt,ISO)    AS date    2005-01-09 Sun  3  1 104565 104564
        ,dy              AS day     2005-01-10 Mon  3  2 104565 104565
        ,WEEK(dt)        AS wk      2005-01-11 Tue  3  2 104565 104565
        ,WEEK_ISO(dt)    AS is      2005-01-12 Wed  3  2 104565 104565
        ,sunday_week(dt) AS sun_wk
        ,monday_week(dt) AS mon_wk
FROM     temp2
ORDER BY 1;
Figure 531, Use week-number functions






CREATE FUNCTION NumList(max_num INTEGER)
RETURNS TABLE(num INTEGER)
LANGUAGE SQL
RETURN 
   WITH temp1 (num) AS
      (VALUES (0)
       UNION ALL
       SELECT  num + 1
       FROM    temp1
       WHERE   num < max_num
      )
   SELECT  num
   FROM    temp1;
Figure 532, Create num-list function






                                                               ANSWERS
SELECT   *                                                     =======
FROM     TABLE(NumList(-1)) AS xxx;                                  0
       
SELECT   *
FROM     TABLE(NumList(+0)) AS xxx;                                  0
       
SELECT   *
FROM     TABLE(NumList(+3)) AS xxx;                                  0
                                                                     1
                                                                     2
                                                                     3
SELECT   *
FROM     TABLE(NumList(CAST(NULL AS INTEGER))) AS xxx;               0
Figure 533, Using num-list function






SELECT   actno                       ANSWER
        ,emstdate                    =================================
        ,emendate                    ACTNO EMSTDATE   EMENDATE   #DAYS
        ,DAYS(emendate) -            ----- ---------- ---------- -----
         DAYS(emstdate) AS #days        70 2002-06-15 2002-07-01    16
FROM     emp_act act                    80 2002-03-01 2002-04-15    45
WHERE    empno   = '000260'
  AND    projno  = 'AD3113'
  AND    actno   <  100
  AND    emptime =  0.5
ORDER BY actno;
Figure 534, Select activity start & end date






SELECT   actno                              ANSWER
        ,#days                              ==========================
        ,num                                ACTNO #DAYS NUM NEW_DATE
        ,emstdate + num DAYS AS new_date    ----- ----- --- ----------
FROM    (SELECT   actno                        70    16   0 2002-06-15
                 ,emstdate                     70    16   1 2002-06-16
                 ,emendate                     70    16   2 2002-06-17
                 ,DAYS(emendate) -             70    16   3 2002-06-18
                  DAYS(emstdate) AS #days      70    16   4 2002-06-19
         FROM     emp_act act                  70    16   5 2002-06-20
         WHERE    empno   = '000260'           70    16   6 2002-06-21
           AND    projno  = 'AD3113'           70    16   7 2002-06-22
           AND    actno   <  100               70    16   8 2002-06-23
           AND    emptime =  0.5               70    16   9 2002-06-24
        )AS aaa                                70    16  10 2002-06-25
        ,TABLE(NumList(#days)) AS ttt          etc...
ORDER BY actno
        ,num;
Figure 535, Generate one row per date between start & end dates (1 of 2)






SELECT   actno
        ,#days
        ,num                                ACTNO #DAYS NUM NEW_DATE
        ,emstdate + num DAYS AS new_date    ----- ----- --- ----------
FROM    (SELECT   actno                        70    16   0 2002-06-15
                 ,emstdate                     70    16   1 2002-06-16
                 ,emendate                     70    16   2 2002-06-17
                 ,DAYS(emendate) -             70    16   3 2002-06-18
                  DAYS(emstdate) AS #days      70    16   4 2002-06-19
         FROM     emp_act act                  70    16   5 2002-06-20
         WHERE    empno   = '000260'           70    16   6 2002-06-21
           AND    projno  = 'AD3113'           70    16   7 2002-06-22
           AND    actno   <  100               70    16   8 2002-06-23
           AND    emptime =  0.5               70    16   9 2002-06-24
        )AS aaa                                70    16  10 2002-06-25
LEFT OUTER JOIN                                etc...
         TABLE(NumList(#days)) AS ttt
ON       1 = 1
ORDER BY actno
        ,num;
Figure 536, Generate one row per date between start & end dates (2 of 2)






CREATE FUNCTION ISCHAR (inval VARCHAR(250))
RETURNS SMALLINT
LANGUAGE SQL
RETURN 
CASE   
   WHEN TRANSLATE(UPPER(inval),' ','ABCDEFGHIJKLMNOPQRSTUVWXYZ') = ' '
   THEN 1
   ELSE 0
END;   
Figure 537, Check if input value is character






CREATE FUNCTION ISNUM (inval VARCHAR(250))
RETURNS SMALLINT
LANGUAGE SQL
RETURN 
CASE   
   WHEN TRANSLATE(inval,' ','01234567890') = ' '
   THEN 1
   ELSE 0
END;   
Figure 538, Check if input value is numeric






WITH temp (indata) AS                                           ANSWER
(VALUES ('ABC'),('123'),('3.4')                             ==========
       ,('-44'),('A1 '),('   '))                            INDATA C N
SELECT  indata         AS indata                            ------ - -
       ,ISCHAR(indata) AS c                                 ABC    1 0
       ,ISNUM(indata)  AS n                                 123    0 1
FROM    temp;                                               3.4    0 0
                                                            -44    0 0
                                                            A1     0 0
                                                                   1 1
Figure 539, Example of functions in use






CREATE FUNCTION ISNUM2 (inval VARCHAR(255))
RETURNS CHAR(4)
LANGUAGE SQL
RETURN 
CASE   
   WHEN  inval                                 = ' '
   THEN  '    '
   WHEN  LOCATE(' ',RTRIM(LTRIM(inval)))       > 0
   THEN  '    '
   WHEN  TRANSLATE(inval,' ','01234567890')    = inval
   THEN  '    '
   WHEN  TRANSLATE(inval,' ','01234567890')    = ' '
   THEN  'INT '
   WHEN  TRANSLATE(inval,' ','+01234567890')   = ' '
    AND  LOCATE('+',LTRIM(inval))              = 1
    AND  LENGTH(REPLACE(inval,'+',''))         = LENGTH(inval) - 1
   THEN  'INT+'
   WHEN  TRANSLATE(inval,' ','-01234567890')   = ' '
    AND  LOCATE('-',LTRIM(inval))              = 1
    AND  LENGTH(REPLACE(inval,'-',''))         = LENGTH(inval) - 1
   THEN  'INT-'
   WHEN  TRANSLATE(inval,' ','.01234567890')   = ' '
    AND  LENGTH(REPLACE(inval,'.',''))         = LENGTH(inval) - 1
   THEN  'DEC '
   WHEN  TRANSLATE(inval,' ','+.01234567890')  = ' '
    AND  LOCATE('+',LTRIM(inval))              = 1
    AND  LENGTH(REPLACE(inval,'+',''))         = LENGTH(inval) - 1
    AND  LENGTH(REPLACE(inval,'.',''))         = LENGTH(inval) - 1
   THEN  'DEC+'
Figure 540, Check if input value is numeric - part 1 of 2






   WHEN  TRANSLATE(inval,' ','-.01234567890')  = ' '
    AND  LOCATE('-',LTRIM(inval))              = 1
    AND  LENGTH(REPLACE(inval,'-',''))         = LENGTH(inval) - 1
    AND  LENGTH(REPLACE(inval,'.',''))         = LENGTH(inval) - 1
   THEN  'DEC-'
   ELSE  '    '
END;   
Figure 541, Check if input value is numeric - part 2 of 2






WITH temp (indata) AS                                           ANSWER
(VALUES ('ABC'),('123'),('3.4')                     ==================
       ,('-44'),('+11'),('-1-')                     INDATA TYPE NUMBER
       ,('12+'),('+.1'),('-0.')                     ------ ---- ------
       ,('   '),('1 1'),(' . '))                    ABC              -
SELECT  indata          AS indata                   123    INT  123.00
       ,ISNUM2(indata)  AS type                     3.4    DEC    3.40
       ,CASE                                        -44    INT- -44.00
           WHEN ISNUM2(indata) <> ''                +11    INT+  11.00
           THEN DEC(indata,5,2)                     -1-              -
           ELSE NULL                                12+              -
        END             AS number                   +.1    DEC+   0.10
FROM    temp;                                       -0.    DEC-   0.00
                                                                     -
                                                    1 1              -
                                                     .               -
Figure 542, Example of function in use






Figure 543, ORDER BY syntax






CREATE VIEW SEQ_DATA(col1,col2)
AS VALUES ('ab','xy')
         ,('AB','xy')
         ,('ac','XY')
         ,('AB','XY')
         ,('Ab','12');
Figure 544, ORDER BY sample data definition






SELECT   col1                                ANSWER        SEQ_DATA
        ,col2                                =========     +---------+
FROM     seq_data                            COL1 COL2     |COL1|COL2|
ORDER BY col1 ASC                            ---- ----     |----+----|
        ,col2;                               ab   xy       |ab  |xy  |
                                             ac   XY       |AB  |xy  |
                                             Ab   12       |ac  |XY  |
                                             AB   xy       |AB  |XY  |
                                             AB   XY       |Ab  |12  |
                                                           +---------+
Figure 545, Simple ORDER BY






SELECT   col1                                                ANSWER
        ,col2                                                =========
FROM     seq_data                                            COL1 COL2
ORDER BY TRANSLATE(col1) ASC                                 ---- ----
        ,TRANSLATE(col2) ASC                                 Ab   12
                                                             ab   xy
                                                             AB   XY
                                                             AB   xy
                                                             ac   XY
Figure 546, Case insensitive ORDER BY






SELECT   col2                                                   ANSWER
FROM     seq_data                                               ======
ORDER BY col1                                                   COL2
        ,col2;                                                  ----
                                                                xy
                                                                XY
                                                                12
                                                                xy
                                                                XY
Figure 547, ORDER BY on not-displayed column






SELECT   col1                                                ANSWER
        ,col2                                                =========
FROM     seq_data                                            COL1 COL2
ORDER BY SUBSTR(col1,2) DESC                                 ---- ----
        ,col2                                                ac   XY
        ,1;                                                  AB   xy
                                                             AB   XY
                                                             Ab   12
                                                             ab   xy
Figure 548, ORDER BY second byte of first column






SELECT   col1                                      ANSWER
        ,HEX(col1) AS hex1                         ===================
        ,col2                                      COL1 HEX1 COL2 HEX2
        ,HEX(col2) AS hex2                         ---- ---- ---- ----
FROM     seq_data                                  AB   4142 XY   5859
ORDER BY HEX(col1)                                 AB   4142 xy   7879
        ,HEX(col2)                                 Ab   4162 12   3132
                                                   ab   6162 xy   7879
                                                   ac   6163 XY   5859
Figure 549, ORDER BY in bit-data sequence






SELECT   col1                                   ANSWER     SEQ_DATA
FROM    (SELECT   col1                          ======     +---------+
         FROM     seq_data                      COL1       |COL1|COL2|
         ORDER BY col2                          ----       |----+----|
        ) AS xxx                                Ab         |ab  |xy  |
ORDER BY ORDER OF xxx;                          ab         |AB  |xy  |
                                                AB         |ac  |XY  |
                                                ac         |AB  |XY  |
                                                AB         |Ab  |12  |
                                                           +---------+
Figure 550, ORDER BY nested ORDER BY






SELECT   *                                                   ANSWER
FROM    (SELECT   *                                          =========
         FROM     (SELECT   *                                COL1 COL2
                   FROM     seq_data                         ---- ----
                   ORDER BY col2                             Ab   12
                  )AS xxx                                    ab   xy
         ORDER BY ORDER OF xxx                               AB   xy
                 ,SUBSTR(col1,2)                             AB   XY
        )AS yyy                                              ac   XY
ORDER BY ORDER OF yyy
        ,col1;
Figure 551, Multiple nested ORDER BY statements






SELECT   empno                                       ANSWER
        ,projno AS prj                               =================
        ,actno  AS act                               EMPNO  PRJ ACT R#
        ,ROW_NUMBER() OVER() AS r#                   ------ --- --- --
FROM     FINAL TABLE                                 400000 ZZZ 999  1
   (INSERT INTO emp_act (empno, projno, actno)       400000 VVV 111  2
    VALUES ('400000','ZZZ',999)
          ,('400000','VVV',111))
ORDER BY INPUT SEQUENCE;
Figure 552, ORDER BY insert input sequence






Figure 553, GROUP BY syntax






GROUP BY division, department, team
GROUP BY division, department
GROUP BY division
GROUP BY division, team
GROUP BY department, team
GROUP BY department
GROUP BY team
GROUP BY ()     <= grand-total
Figure 554, Possible groupings






GROUP BY division, department, team
UNION ALL
GROUP BY division, department
UNION ALL
GROUP BY division
UNION ALL
GROUP BY ()
       
       
GROUP BY GROUPING SETS ((division, department, team)
                       ,(division, department)
                       ,(division)
                       ,())
       
       
GROUP BY ROLLUP (division, department, team)
Figure 555, Three ways to write the same GROUP BY






CREATE VIEW employee_view AS                        ANSWER
SELECT   SUBSTR(workdept,1,1) AS d1                 ==================
        ,workdept             AS dept               D1 DEPT SEX SALARY
        ,sex                  AS sex                -- ---- --- ------
        ,INTEGER(salary)      AS salary             A  A00  F    52750
FROM     employee                                   A  A00  M    29250
WHERE    workdept < 'D20';                          A  A00  M    46500
COMMIT;                                             B  B01  M    41250
                                                    C  C01  F    23800
                                                    C  C01  F    28420
                                                    C  C01  F    38250
                                                    D  D11  F    21340
SELECT   *                                          D  D11  F    22250
FROM     employee_view                              D  D11  F    29840
ORDER BY 1,2,3,4;                                   D  D11  M    18270
                                                    D  D11  M    20450
                                                    D  D11  M    24680
                                                    D  D11  M    25280
                                                    D  D11  M    27740
                                                    D  D11  M    32250
Figure 556, GROUP BY Sample Data






SELECT   d1, dept, sex                        ANSWER
        ,SUM(salary)        AS salary         ========================
        ,SMALLINT(COUNT(*)) AS #rows          D1 DEPT SEX SALARY #ROWS
FROM     employee_view                        -- ---- --- ------ -----
WHERE    dept <> 'ABC'                        A  A00  F    52750     1
GROUP BY d1, dept, sex                        A  A00  M    75750     2
HAVING   dept        > 'A0'                   B  B01  M    41250     1
   AND  (SUM(salary) > 100                    C  C01  F    90470     3
    OR   MIN(salary) >  10                    D  D11  F    73430     3
    OR   COUNT(*)   <>  22)                   D  D11  M   148670     6
ORDER BY d1, dept, sex;
Figure 557, Simple GROUP BY






SELECT   sex                                          ANSWER
        ,SUM(salary)        AS salary                 ================
        ,SMALLINT(COUNT(*)) AS #rows                  SEX SALARY #ROWS
FROM     employee_view                                --- ------ -----
WHERE    sex IN ('F','M')                             F    52750     1
GROUP BY dept                                         F    90470     3
        ,sex                                          F    73430     3
ORDER BY sex;                                         M    75750     2
                                                      M    41250     1
                                                      M   148670     6
Figure 558, GROUP BY on non-displayed field






SELECT   SUM(salary)        AS salary                     ANSWER
        ,SMALLINT(COUNT(*)) AS #rows                      ============
FROM     employee_view                                    SALARY #ROWS
WHERE    d1 <> 'X'                                        ------ -----
GROUP BY SUBSTR(dept,3,1)                                 128500     3
HAVING   COUNT(*) <> 99;                                  353820    13
Figure 559, GROUP BY on derived field, not shown






SELECT   SUBSTR(dept,3,1)   AS wpart                ANSWER
        ,SUM(salary)        AS salary               ==================
        ,SMALLINT(COUNT(*)) AS #rows                WPART SALARY #ROWS
FROM     employee_view                              ----- ------ -----
GROUP BY SUBSTR(dept,3,1)                           1     353820    13
ORDER BY wpart DESC;                                0     128500     3
Figure 560, GROUP BY on derived field, shown






GROUP BY GROUPING SETS ((A,B,C))      is equivalent to      GROUP BY A
                                                                    ,B
                                                                    ,C
       
GROUP BY GROUPING SETS (A,B,C)        is equivalent to      GROUP BY A
                                                            UNION ALL
                                                            GROUP BY B
                                                            UNION ALL
                                                            GROUP BY C
       
GROUP BY GROUPING SETS (A,(B,C))      is equivalent to      GROUP BY A
                                                            UNION ALL
                                                            GROUP BY B
                                                                 ,BY C
Figure 561, GROUPING SETS in parenthesis vs. not






GROUP BY GROUPING SETS (A)            is equivalent to      GROUP BY A
        ,GROUPING SETS (B)                                          ,B
        ,GROUPING SETS (C)                                          ,C
       
GROUP BY GROUPING SETS (A)            is equivalent to      GROUP BY A
        ,GROUPING SETS ((B,C))                                      ,B
                                                                    ,C
       
GROUP BY GROUPING SETS (A)            is equivalent to      GROUP BY A
        ,GROUPING SETS (B,C)                                        ,B
                                                            UNION ALL
                                                            GROUP BY A
                                                                    ,C
Figure 562, Multiple GROUPING SETS






GROUP BY A                            is equivalent to      GROUP BY A
        ,GROUPING SETS ((B,C))                                      ,B
                                                                    ,C
Figure 563, Simple GROUP BY expression and GROUPING SETS combined






GROUP BY A                            is equivalent to      GROUP BY A
        ,B                                                          ,B
        ,GROUPING SETS ((B,C))                                      ,C
       
GROUP BY A                            is equivalent to      GROUP BY A
        ,B                                                          ,B
        ,GROUPING SETS (B,C)                                        ,C
                                                            UNION ALL
                                                            GROUP BY A
                                                                    ,B
       
GROUP BY A                            is equivalent to      GROUP BY A
        ,B                                                          ,B
        ,C                                                          ,C
        ,GROUPING SETS (B,C)                                UNION ALL
                                                            GROUP BY A
                                                                    ,B
                                                                    ,C
Figure 564, Mixing simple GROUP BY expressions and GROUPING SETS






GROUP BY GROUPING SETS ((A,B,C)       is equivalent to      GROUP BY A
                       ,(A,B)                                       ,B
                       ,(C))                                        ,C
                                                            UNION ALL
                                                            GROUP BY A
                                                                    ,B
                                                            UNION ALL
                                                            GROUP BY C
       
GROUP BY GROUPING SETS ((A)           is equivalent to      GROUP BY A
                       ,(B,C)                               UNION ALL
                       ,(A)                                 GROUP BY B
                       ,A                                           ,C
                       ,((C)))                              UNION ALL
                                                            GROUP BY A
                                                            UNION ALL
                                                            GROUP BY A
                                                            UNION ALL
                                                            GROUP BY C
Figure 565, GROUPING SETS with multiple components






GROUP BY GROUPING SETS ((A,B,C)      is equivalent to      GROUP BY A
                       ,(A,B)                                      ,B
                       ,(A)                                        ,C
                       ,())                                UNION ALL
                                                           GROUP BY A
                                                                   ,B
is equivalent to                                           UNION ALL
                                                           GROUP BY A
                                                           UNION ALL
ROLLUP(A,B,C)                                              grand-totl
Figure 566, GROUPING SET with multiple components, using grand-total






GROUP BY GROUPING SETS ((A,B,C)       is equivalent to      GROUP BY A
                       ,(A,B)                                       ,B
                       ,(A,C)                                       ,C
                       ,(B,C)                               UNION ALL
                       ,(A)                                 GROUP BY A
                       ,(B)                                         ,B
                       ,(C)                                 UNION ALL
                       ,())                                 GROUP BY A
                                                                    ,C
                                                            UNION ALL
                                                            GROUP BY B
is equivalent to                                                    ,C
                                                            UNION ALL
                                                            GROUP BY A
                                                            UNION ALL
CUBE(A,B,C)                                                 GROUP BY B
                                                            UNION ALL
                                                            GROUP BY C
                                                            UNION ALL
                                                            grand-totl
Figure 567, GROUPING SET with multiple components, using grand-total






SELECT   d1                             ANSWER
        ,dept                           ==============================
        ,sex                            D1 DEPT SEX   SAL  #R DF WF SF
        ,SUM(salary)        AS sal      -- ---- --- ------ -- -- -- --
        ,SMALLINT(COUNT(*)) AS #r       A  A00  F    52750  1  0  0  0
        ,GROUPING(d1)       AS f1       A  A00  M    75750  2  0  0  0
        ,GROUPING(dept)     AS fd       B  B01  M    41250  1  0  0  0
        ,GROUPING(sex)      AS fs       C  C01  F    90470  3  0  0  0
FROM     employee_view                  D  D11  F    73430  3  0  0  0
GROUP BY GROUPING SETS (d1)             D  D11  M   148670  6  0  0  0
        ,GROUPING SETS ((dept,sex))
ORDER BY d1
        ,dept
        ,sex;
Figure 568, Multiple GROUPING SETS, making one GROUP BY






SELECT   d1                             ANSWER
        ,dept                           ==============================
        ,sex                            D1 DEPT SEX   SAL  #R F1 FD FS
        ,SUM(salary)        AS sal      -- ---- --- ------ -- -- -- --
        ,SMALLINT(COUNT(*)) AS #r       A   A00 -   128500  3  0  0  1
        ,GROUPING(d1)       AS f1       A   -   F    52750  1  0  1  0
        ,GROUPING(dept)     AS fd       A   -   M    75750  2  0  1  0
        ,GROUPING(sex)      AS fs       B   B01 -    41250  1  0  0  1
FROM     employee_view                  B   -   M    41250  1  0  1  0
GROUP BY GROUPING SETS (d1)             C   C01 -    90470  3  0  0  1
        ,GROUPING SETS (dept,sex)       C   -   F    90470  3  0  1  0
ORDER BY d1                             D   D11 -   222100  9  0  0  1
        ,dept                           D   -   F    73430  3  0  1  0
        ,sex;                           D   -   M   148670  6  0  1  0
Figure 569, Multiple GROUPING SETS, making two GROUP BY results






SELECT   d1                             ANSWER
        ,dept                           ==============================
        ,sex                            D1 DEPT SEX SAL    #R F1 FD FS
        ,SUM(salary)        AS sal      ------------------------------
        ,SMALLINT(COUNT(*)) AS #r       A  A00  F    52750  1  0  0  0
        ,GROUPING(d1)       AS f1       A  A00  M    75750  2  0  0  0
        ,GROUPING(dept)     AS fd       B  B01  M    41250  1  0  0  0
        ,GROUPING(sex)      AS fs       C  C01  F    90470  3  0  0  0
FROM     employee_view                  D  D11  F    73430  3  0  0  0
GROUP BY d1                             D  D11  M   148670  6  0  0  0
        ,dept
        ,GROUPING SETS ((dept,sex))
ORDER BY d1
        ,dept
        ,sex;
Figure 570, Repeated field essentially ignored






SELECT   d1                             ANSWER
        ,dept                           ==============================
        ,sex                            D1 DEPT SEX SAL    #R F1 FD FS
        ,SUM(salary)        AS sal      ------------------------------
        ,SMALLINT(COUNT(*)) AS #r       A  A00  F    52750  1  0  0  0
        ,GROUPING(d1)       AS f1       A  A00  M    75750  2  0  0  0
        ,GROUPING(dept)     AS fd       A  A00  -   128500  3  0  0  1
        ,GROUPING(sex)      AS fs       B  B01  M    41250  1  0  0  0
FROM     employee_view                  B  B01  -    41250  1  0  0  1
GROUP BY d1                             C  C01  F    90470  3  0  0  0
        ,DEPT                           C  C01  -    90470  3  0  0  1
        ,GROUPING SETS (dept,sex)       D  D11  F    73430  3  0  0  0
ORDER BY d1                             D  D11  M   148670  6  0  0  0
        ,dept                           D  D11  -   222100  9  0  0  1
        ,sex;
Figure 571, Repeated field impacts query result






GROUP BY d1                           is equivalent to   GROUP BY d1
        ,dept                                                    ,dept
        ,GROUPING SETS ((dept,sex))                               sex
       
       
GROUP BY d1                           is equivalent to   GROUP BY d1
        ,dept                                                    ,dept
        ,GROUPING SETS (dept,sex)                                 sex
                                                         UNION ALL
                                                         GROUP BY d1
                                                                 ,dept
                                                                 ,dept
Figure 572, Repeated field impacts query result






GROUP BY ROLLUP(A,B,C)       ===>       GROUP BY GROUPING SETS((A,B,C)
                                                              ,(A,B)
                                                              ,(A)
                                                              ,())
       
GROUP BY ROLLUP(C,B)         ===>       GROUP BY GROUPING SETS((C,B)
                                                              ,(C)
                                                              ,())
       
GROUP BY ROLLUP(A)           ===>       GROUP BY GROUPING SETS((A)
                                                              ,())
Figure 573, ROLLUP vs. GROUPING SETS






GROUP BY ROLLUP(A,(B,C))     ===>       GROUP BY GROUPING SETS((A,B,C)
                                                              ,(A)
                                                              ,())
Figure 574, ROLLUP vs. GROUPING SETS






GROUP BY ROLLUP(A)           ===>       GROUP BY GROUPING SETS((A,B,C)
        ,ROLLUP(B,C)                                          ,(A,B)
                                                              ,(A)
                                                              ,(B,C)
                                                              ,(B)
                                                              ,())
Figure 575, ROLLUP vs. GROUPING SETS






ROLLUP(A)           *   ROLLUP(B,C)          =   GROUPING SETS((A,B,C)
                                                              ,(A,B)
                                                              ,(A)
GROUPING SETS((A)   *   GROUPING SETS((B,C)  =                ,(B,C)
             ,())                    ,(B)                     ,(B)
                                      ())                     ,(())
Figure 576, Multiplying GROUPING SETS






SELECT   dept                                     ANSWER
        ,SUM(salary)        AS salary             ====================
        ,SMALLINT(COUNT(*)) AS #rows              DEPT SALARY #ROWS FD
        ,GROUPING(dept)     AS fd                 ---- ------ ----- --
FROM     employee_view                            A00  128500     3 0
GROUP BY dept                                     B01   41250     1 0
ORDER BY dept;                                    C01   90470     3 0
                                                  D11  222100     9 0
Figure 577, Simple GROUP BY






SELECT   dept                                     ANSWER
        ,SUM(salary)        AS salary             ====================
        ,SMALLINT(COUNT(*)) AS #rows              DEPT SALARY #ROWS FD
        ,GROUPING(dept)     AS FD                 ---- ------ ----- --
FROM     employee_view                            A00  128500     3  0
GROUP BY ROLLUP(dept)                             B01   41250     1  0
ORDER BY dept;                                    C01   90470     3  0
                                                  D11  222100     9  0
                                                  -    482320    16  1
Figure 578, GROUP BY with ROLLUP






SELECT   dept                                     ANSWER
        ,SUM(salary)           AS salary          ====================
        ,SMALLINT(COUNT(*))    AS #rows           DEPT SALARY #ROWS FD
        ,GROUPING(dept)        AS fd              ---- ------ ----- --
FROM     employee_view                            A00  128500     3  0
GROUP BY dept                                     B01   41250     1  0
UNION ALL                                         C01   90470     3  0
SELECT   CAST(NULL AS CHAR(3)) AS dept            D11  222100     9  0
        ,SUM(salary)           AS salary          -    482320    16  1
        ,SMALLINT(COUNT(*))    AS #rows
        ,CAST(1 AS INTEGER)    AS fd
FROM     employee_view
ORDER BY dept;
Figure 579, ROLLUP done the old-fashioned way






SELECT   dept                                     ANSWER
        ,SUM(salary)        AS salary             ====================
        ,SMALLINT(COUNT(*)) AS #rows              DEPT SALARY #ROWS FD
        ,GROUPING(dept)     AS fd                 ---- ------ ----- --
FROM     employee_view                            A00  128500     3  0
GROUP BY dept                                     A00  128500     3  0
        ,ROLLUP(dept)                             B01   41250     1  0
ORDER BY dept;                                    B01   41250     1  0
                                                  C01   90470     3  0
                                                  C01   90470     3  0
                                                  D11  222100     9  0
                                                  D11  222100     9  0
Figure 580, Repeating a field in GROUP BY and ROLLUP (error)






GROUP BY dept        => GROUP BY dept                 => GROUP BY dept
        ,ROLLUP(dept)           ,GROUPING SETS((dept)    UNION ALL
                                              ,())       GROUP BY dept
                                                                 ,()
Figure 581, Repeating a field, explanation






SELECT   dept                              ANSWER
        ,sex                               ===========================
        ,SUM(salary)        AS salary      DEPT SEX SALARY #ROWS FD FS
        ,SMALLINT(COUNT(*)) AS #rows       ---- --- ------ ----- -- --
        ,GROUPING(dept)     AS fd          A00  F    52750     1  0  0
        ,GROUPING(sex)      AS fs          A00  M    75750     2  0  0
FROM     employee_view                     A00  -   128500     3  0  1
GROUP BY dept                              B01  M    41250     1  0  0
        ,ROLLUP(sex)                       B01  -    41250     1  0  1
ORDER BY dept                              C01  F    90470     3  0  0
        ,sex;                              C01  -    90470     3  0  1
                                           D11  F    73430     3  0  0
                                           D11  M   148670     6  0  0
                                           D11  -   222100     9  0  1
Figure 582, GROUP BY on 1st field, ROLLUP on 2nd






SELECT   dept                              ANSWER
        ,sex                               ===========================
        ,SUM(salary)        AS salary      DEPT SEX SALARY #ROWS FD FS
        ,SMALLINT(COUNT(*)) AS #rows       ---- --- ------ ----- -- --
        ,GROUPING(dept)     AS fd          A00  F    52750     1  0  0
        ,GROUPING(sex)      AS fs          A00  M    75750     2  0  0
FROM     employee_view                     A00  -   128500     3  0  1
GROUP BY ROLLUP(dept                       B01  M    41250     1  0  0
               ,sex)                       B01  -    41250     1  0  1
ORDER BY dept                              C01  F    90470     3  0  0
        ,sex;                              C01  -    90470     3  0  1
                                           D11  F    73430     3  0  0
                                           D11  M   148670     6  0  0
                                           D11  -   222100     9  0  1
                                           -    -   482320    16  1  1
Figure 583, ROLLUP on DEPT, then SEX






SELECT   sex                               ANSWER
        ,dept                              ===========================
        ,SUM(salary)        AS salary      SEX DEPT SALARY #ROWS FD FS
        ,SMALLINT(COUNT(*)) AS #rows       --- ---- ------ ----- -- --
        ,GROUPING(dept)     AS fd          F   A00   52750     1  0  0
        ,GROUPING(sex)      AS fs          F   C01   90470     3  0  0
FROM     employee_view                     F   D11   73430     3  0  0
GROUP BY ROLLUP(sex                        F   -    216650     7  1  0
               ,dept)                      M   A00   75750     2  0  0
ORDER BY sex                               M   B01   41250     1  0  0
        ,dept;                             M   D11  148670     6  0  0
                                           M   -    265670     9  1  0
                                           -   -    482320    16  1  1
Figure 584, ROLLUP on SEX, then DEPT






SELECT   sex                               ANSWER
        ,dept                              ===========================
        ,SUM(salary)        AS salary      SEX DEPT SALARY #ROWS FD FS
        ,SMALLINT(COUNT(*)) AS #rows       --- ---- ------ ----- -- --
        ,GROUPING(dept)     AS fd          F   A00   52750     1  0  0
        ,GROUPING(sex)      AS fs          F   C01   90470     3  0  0
FROM     employee_view                     F   D11   73430     3  0  0
GROUP BY GROUPING SETS ((sex, dept)        F   -    216650     7  1  0
                       ,(sex)              M   A00   75750     2  0  0
                       ,())                M   B01   41250     1  0  0
ORDER BY sex                               M   D11  148670     6  0  0
        ,dept;                             M   -    265670     9  1  0
                                           -   -    482320    16  1  1
Figure 585, ROLLUP on SEX, then DEPT






SELECT   sex                               ANSWER
        ,dept                              ===========================
        ,SUM(salary)        AS salary      SEX DEPT SALARY #ROWS FD FS
        ,SMALLINT(COUNT(*)) AS #rows       --- ---- ------ ----- -- --
        ,GROUPING(dept)     AS fd          F   A00   52750     1  0  0
        ,GROUPING(sex)      AS fs          F   C01   90470     3  0  0
FROM     employee_view                     F   D11   73430     3  0  0
GROUP BY ROLLUP(sex)                       F   -    216650     7  1  0
        ,ROLLUP(dept)                      M   A00   75750     2  0  0
ORDER BY sex                               M   B01   41250     1  0  0
        ,dept;                             M   D11  148670     6  0  0
                                           M   -    265670     9  1  0
                                           -   A00  128500     3  0  1
                                           -   B01   41250     1  0  1
                                           -   C01   90470     3  0  1
                                           -   D11  222100     9  0  1
                                           -   -    482320    16  1  1
Figure 586, Two independent ROLLUPS






SELECT   dept                              ANSWER
        ,sex                               ===========================
        ,SUM(salary)        AS salary      DEPT SEX SALARY #ROWS FD FS
        ,SMALLINT(COUNT(*)) AS #rows       ---- --- ------ ----- -- --
        ,GROUPING(dept)     AS fd          A00  F    52750     1  0  0
        ,GROUPING(sex)      AS fs          A00  M    75750     2  0  0
FROM     employee_view                     B01  M    41250     1  0  0
GROUP BY ROLLUP((dept,sex))                C01  F    90470     3  0  0
ORDER BY dept                              D11  F    73430     3  0  0
        ,sex;                              D11  M   148670     6  0  0
                                           -    -   482320    16  1  1
Figure 587, Combined-field ROLLUP






SELECT   SUM(salary)        AS salary                     ANSWER
        ,SMALLINT(COUNT(*)) AS #rows                      ============
FROM     employee_view                                    SALARY #ROWS
GROUP BY ROLLUP(sex                                       ------ -----
               ,dept)                                     482320    16
HAVING   GROUPING(dept) = 1
   AND   GROUPING(sex)  = 1
ORDER BY salary;
Figure 588, Use HAVING to get only grand-total row






SELECT   SUM(salary)        AS salary                     ANSWER
        ,SMALLINT(COUNT(*)) AS #rows                      ============
FROM     employee_view                                    SALARY #ROWS
GROUP BY GROUPING SETS(());                               ------ -----
                                                          482320    16
Figure 589, Use GROUPING SETS to get grand-total row






SELECT   SUM(salary)        AS salary                     ANSWER
        ,SMALLINT(COUNT(*)) AS #rows                      ============
FROM     employee_view                                    SALARY #ROWS
GROUP BY ();                                              ------ -----
                                                          482320    16
Figure 590, Use GROUP BY to get grand-total row






SELECT   SUM(salary)        AS salary                     ANSWER
        ,SMALLINT(COUNT(*)) AS #rows                      ============
FROM     employee_view;                                   SALARY #ROWS
                                                          ------ -----
                                                          482320    16
Figure 591, Get grand-total row directly






GROUP BY CUBE(A,B,C)         ===>       GROUP BY GROUPING SETS((A,B,C)
                                                              ,(A,B)
                                                              ,(A,C)
                                                              ,(B,C)
                                                              ,(A)
                                                              ,(B)
                                                              ,(C)
                                                              ,())
       
GROUP BY CUBE(C,B)           ===>       GROUP BY GROUPING SETS((C,B)
                                                              ,(C)
                                                              ,(B)
                                                              ,())
       
GROUP BY CUBE(A)             ===>       GROUP BY GROUPING SETS((A)
                                                              ,())
Figure 592, CUBE vs. GROUPING SETS






GROUP BY CUBE(A,(B,C))       ===>       GROUP BY GROUPING SETS((A,B,C)
                                                              ,(B,C)
                                                              ,(A)
                                                              ,())
Figure 593, CUBE vs. GROUPING SETS






GROUP BY CUBE(A,B)    ==>    GROUPING SETS((A,B,C),(A,B),(A,B,C),(A,B)
        ,CUBE(B,C)                        ,(A,B,C),(A,B),(A,C),(A)
                                          ,(B,C),(B),(B,C),(B)
                                          ,(B,C),(B),(C),())
Figure 594, CUBE vs. GROUPING SETS






SELECT   d1                             ANSWER
        ,dept                           ==============================
        ,sex                            D1 DEPT SEX   SAL  #R F1 FD FS
        ,INT(SUM(salary))   AS sal      -- ---- --- ------ -- -- -- --
        ,SMALLINT(COUNT(*)) AS #r       A  A00  F    52750  1  0  0  0
        ,GROUPING(d1)       AS f1       A  A00  M    75750  2  0  0  0
        ,GROUPING(dept)     AS fd       A  A00  -   128500  3  0  0  1
        ,GROUPING(sex)      AS fs       A  -    F    52750  1  0  1  0
FROM     employee_view                  A  -    M    75750  2  0  1  0
GROUP BY CUBE(d1, dept, sex)            A  -    -   128500  3  0  1  1
ORDER BY d1                             B  B01  M    41250  1  0  0  0
        ,dept                           B  B01  -    41250  1  0  0  1
        ,sex;                           B  -    M    41250  1  0  1  0
                                        B  -    -    41250  1  0  1  1
                                        C  C01  F    90470  3  0  0  0
                                        C  C01  -    90470  3  0  0  1
                                        C  -    F    90470  3  0  1  0
                                        C  -    -    90470  3  0  1  1
                                        D  D11  F    73430  3  0  0  0
                                        D  D11  M   148670  6  0  0  0
                                        D  D11  -   222100  9  0  0  1
                                        D  -    F    73430  3  0  1  0
                                        D  -    M   148670  6  0  1  0
                                        D  -    -   222100  9  0  1  1
                                        -  A00  F    52750  1  1  0  0
                                        -  A00  M    75750  2  1  0  0
                                        -  A00  -   128500  3  1  0  1
                                        -  B01  M    41250  1  1  0  0
                                        -  B01  -    41250  1  1  0  1
                                        -  C01  F    90470  3  1  0  0
                                        -  C01  -    90470  3  1  0  1
                                        -  D11  F    73430  3  1  0  0
                                        -  D11  M   148670  6  1  0  0
                                        -  D11  -   222100  9  1  0  1
                                        -  -    F   216650  7  1  1  0
                                        -  -    M   265670  9  1  1  0
                                        -  -    -   482320 16  1  1  1
Figure 595, CUBE example






SELECT   d1                             ANSWER
        ,dept                           ==============================
        ,sex                            D1 DEPT SEX   SAL  #R F1 FD FS
        ,INT(SUM(salary))   AS sal      -- ---- --- ------ -- -- -- --
        ,SMALLINT(COUNT(*)) AS #r       A  A00  F    52750  1  0  0  0
        ,GROUPING(d1)       AS f1       A  A00  M    75750  2  0  0  0
        ,GROUPING(dept)     AS fd       etc... (same as prior query)
        ,GROUPING(sex)      AS fs
FROM     employee_view
GROUP BY GROUPING SETS ((d1, dept, sex)
                       ,(d1,dept)
                       ,(d1,sex)
                       ,(dept,sex)
                       ,(d1)
                       ,(dept)
                       ,(sex)
                       ,())
ORDER BY d1
        ,dept
        ,sex;
Figure 596, CUBE expressed using multiple GROUPING SETS






SELECT   d1                             ANSWER
        ,dept                           ==============================
        ,sex                            D1 DEPT SEX  SAL   #R F1 FD FS
        ,INT(SUM(salary))   AS sal      ------------------------------
        ,SMALLINT(COUNT(*)) AS #r       A  A00  F    52750  1  0  0  0
        ,GROUPING(d1)       AS f1       A  A00  M    75750  2  0  0  0
        ,GROUPING(dept)     AS fd       B  B01  M    41250  1  0  0  0
        ,GROUPING(sex)      AS fs       C  C01  F    90470  3  0  0  0
FROM     employee_VIEW                  D  D11  F    73430  3  0  0  0
GROUP BY CUBE((d1, dept, sex))          D  D11  M   148670  6  0  0  0
ORDER BY d1                             -  -    -   482320 16  1  1  1
        ,dept
        ,sex;
Figure 597, CUBE on compound fields






GROUP BY CUBE((A,B,C)) => GROUP BY GROUING SETS((A,B,C) =>  GROUP BY A
                                               ,())                 ,B
                                                                    ,C
                                                            UNION ALL
                                                            GROUP BY()
Figure 598, CUBE on compound field, explanation






SELECT   d1                 AS d1                   ANSWER
        ,dept               AS dpt                  ==================
        ,sex                AS sx                   D1 DPT SX   SAL  R
        ,INT(SUM(salary))   AS sal                  -- --- -- ------ -
        ,SMALLINT(COUNT(*)) AS r                    A  A00 F   52750 1
FROM     employee_VIEW                              A  A00 M   75750 2
GROUP BY d1                                         B  B01 M   41250 1
        ,dept                                       C  C01 F   90470 3
        ,sex                                        D  D11 F   73430 3
ORDER BY 1,2,3;                                     D  D11 M  148670 6
Figure 599, Basic GROUP BY example






DESIRED SUB-TOTALS               EQUIVILENT TO
==================               =====================================
D1, DEPT, and SEX.               GROUP BY GROUPING SETS ((d1,dept,sex)
D1 and DEPT.                                            ,(d1,dept)
D1 and SEX.                                             ,(d1,sex)
D1.                                                     ,(d1)
SEX.                                                    ,(sex)
Grand total.                     EQUIVILENT TO          ,())
                                 =======================
                                 GROUP BY ROLLUP(d1,dept)
                                         ,ROLLUP(sex)
Figure 600, Sub-totals that we want to get






SELECT   *
FROM    (SELECT   d1                        AS d1
                 ,dept                      AS dpt
                 ,sex                       AS sx
                 ,INT(SUM(salary))          AS sal
                 ,SMALLINT(COUNT(*))        AS #r
                 ,SMALLINT(GROUPING(d1))    AS g1
                 ,SMALLINT(GROUPING(dept))  AS gd
                 ,SMALLINT(GROUPING(sex))   AS gs
         FROM     EMPLOYEE_VIEW                                 ANSWER
         GROUP BY CUBE(d1,dept,sex)       ============================
        )AS xxx                           D1 DPT SX  SAL   #R G1 GD GS
WHERE    (g1,gd,gs) = (0,0,0)             -- --- -- ------ -- -- -- --
   OR    (g1,gd,gs) = (0,0,1)             A  A00 F   52750  1  0  0  0
   OR    (g1,gd,gs) = (0,1,0)             A  A00 M   75750  2  0  0  0
   OR    (g1,gd,gs) = (0,1,1)             A  A00 -  128500  3  0  0  1
   OR    (g1,gd,gs) = (1,1,0)             A  -   F   52750  1  0  1  0
   OR    (g1,gd,gs) = (1,1,1)             A  -   M   75750  2  0  1  0
ORDER BY 1,2,3;                           A  -   -  128500  3  0  1  1
                                          B  B01 M   41250  1  0  0  0
                                          B  B01 -   41250  1  0  0  1
                                          B  -   M   41250  1  0  1  0
                                          B  -   -   41250  1  0  1  1
                                          C  C01 F   90470  3  0  0  0
                                          C  C01 -   90470  3  0  0  1
                                          C  -   F   90470  3  0  1  0
                                          C  -   -   90470  3  0  1  1
                                          D  D11 F   73430  3  0  0  0
                                          D  D11 M  148670  6  0  0  0
                                          D  D11 -  222100  9  0  0  1
                                          D  -   F   73430  3  0  1  0
                                          D  -   M  148670  6  0  1  0
                                          D  -   -  222100  9  0  1  1
                                          -  -   F  216650  7  1  1  0
                                          -  -   M  265670  9  1  1  0
                                          -  -   -  482320 16  1  1  1
Figure 601, Get lots of sub-totals, using CUBE






(G1,GD,GS) = (0,0,0)   <==   D1, DEPT, SEX
(G1,GD,GS) = (0,0,1)   <==   D1, DEPT
(G1,GD,GS) = (0,1,0)   <==   D1, SEX
(G1,GD,GS) = (0,1,1)   <==   D1,
(G1,GD,GS) = (1,1,0)   <==   SEX,
(G1,GD,GS) = (1,1,1)   <==   grand total
Figure 602, Predicates used - explanation






SELECT   d1                                      ANSWER
        ,dept                                    =====================
        ,sex                                     D1 DEPT SEX   SAL  #R
        ,INT(SUM(salary))   AS sal               -- ---- --- ------ --
        ,SMALLINT(COUNT(*)) AS #r                A  A00  F    52750  1
FROM     employee_view                           A  A00  M    75750  2
GROUP BY ROLLUP(d1,dept)                         A  A00  -   128500  3
        ,ROLLUP(sex)                             A  -    F    52750  1
ORDER BY 1,2,3;                                  A  -    M    75750  2
                                                 A  -    -   128500  3
                                                 B  B01  M    41250  1
                                                 B  B01  -    41250  1
                                                 B  -    M    41250  1
                                                 B  -    -    41250  1
                                                 C  C01  F    90470  3
                                                 C  C01  -    90470  3
                                                 C  -    F    90470  3
                                                 C  -    -    90470  3
                                                 D  D11  F    73430  3
                                                 D  D11  M   148670  6
                                                 D  D11  -   222100  9
                                                 D  -    F    73430  3
                                                 D  -    M   148670  6
                                                 D  -    -   222100  9
                                                 -  -    F   216650  7
                                                 -  -    M   265670  9
                                                 -  -    -   482320 16
Figure 603, Get lots of sub-totals, using ROLLUP






SELECT   dept, job
        ,COUNT(*)
FROM     staff
GROUP BY dept, job
ORDER BY dept, job;
Figure 604, GROUP BY with ORDER BY






WITH staff2 (dept, avgsal) AS                        ANSWER
  (SELECT   dept                                     =================
           ,AVG(salary)                              ID  NAME     DEPT
   FROM     staff                                    --- -------- ----
   GROUP BY dept                                     160 Molinare   10
   HAVING   AVG(salary) > 18000                      210 Lu         10
  )                                                  240 Daniels    10
SELECT   a.id                                        260 Jones      10
        ,a.name
        ,a.dept
FROM     staff  a
        ,staff2 b
WHERE    a.dept = b.dept
ORDER BY a.id;
Figure 605, GROUP BY on one side of join - using common table expression






SELECT   a.id                                        ANSWER
        ,a.name                                      =================
        ,a.dept                                      ID  NAME     DEPT
FROM     staff  a                                    --- -------- ----
        ,(SELECT   dept        AS dept               160 Molinare   10
                  ,AVG(salary) AS avgsal             210 Lu         10
          FROM     staff                             240 Daniels    10
          GROUP BY dept                              260 Jones      10
          HAVING   AVG(salary) > 18000
         )AS b
WHERE    a.dept = b.dept
ORDER BY a.id;
Figure 606, GROUP BY on one side of join - using fullselect






SELECT   COUNT(*)  AS c1                                        ANSWER
FROM     staff                                                  ======
WHERE    id < 1;                                                     0
       
       
SELECT   COUNT(*)  AS c1                                        ANSWER
FROM     staff                                                  ======
WHERE    id < 1                                                 no row
GROUP BY id;
Figure 607, COUNT and No Rows






CREATE VIEW staff_v1 AS                    STAFF_V1        STAFF_V2
SELECT id, name                            +-----------+   +---------+
FROM   staff                               |ID|NAME    |   |ID|JOB   |
WHERE  ID BETWEEN 10 AND 30;               |--|--------|   |--|------|
                                           |10|Sanders |   |20|Sales |
CREATE VIEW staff_v2 AS                    |20|Pernal  |   |30|Clerk |
SELECT id, job                             |30|Marenghi|   |30|Mgr   |
FROM   staff                               +-----------+   |40|Sales |
WHERE  id BETWEEN 20 AND 50                                |50|Mgr   |
UNION ALL                                                  +---------+
SELECT id, 'Clerk' AS job
FROM   staff
WHERE  id = 30;
Figure 608, Sample Views used in Join Examples






Figure 609, Join Syntax #1






SELECT   v1.id                                       JOIN ANSWER
        ,v1.name                                     =================
        ,v2.job                                      ID NAME     JOB
FROM     staff_v1 v1                                 -- -------- -----
        ,staff_v2 v2                                 20 Pernal   Sales
WHERE    v1.id = v2.id                               30 Marenghi Clerk
ORDER BY v1.id                                       30 Marenghi Mgr
        ,v2.job;
Figure 610, Sample two-table join






SELECT   v1.id                                       JOIN ANSWER
        ,v2.job                                      =================
        ,v3.name                                     ID JOB   NAME
FROM     staff_v1 v1                                 -- ----- --------
        ,staff_v2 v2                                 30 Clerk Marenghi
        ,staff_v1 v3                                 30 Mgr   Marenghi
WHERE    v1.id = v2.id
  AND    v2.id = v3.id
  AND    v3.name LIKE 'M%'
ORDER BY v1.name
        ,v2.job;
Figure 611, Sample three-table join






Figure 612, Join Syntax #2






SELECT   v1.id                                       JOIN ANSWER
        ,v1.name                                     =================
        ,v2.job                                      ID NAME     JOB
FROM     staff_v1 v1                                 -- -------- -----
INNER JOIN                                           20 Pernal   Sales
         staff_v2 v2                                 30 Marenghi Clerk
ON       v1.id = v2.id                               30 Marenghi Mgr
ORDER BY v1.id
        ,v2.job;
Figure 613, Sample two-table inner join






SELECT   v1.id                            STAFF_V1        STAFF_V2
        ,v2.job                           +-----------+   +---------+
        ,v3.name                          |ID|NAME    |   |ID|JOB   |
FROM     staff_v1 v1                      |--|--------|   |--|------|
JOIN                                      |10|Sanders |   |20|Sales |
         staff_v2 v2                      |20|Pernal  |   |30|Clerk |
ON       v1.id = v2.id                    |30|Marenghi|   |30|Mgr   |
JOIN                                      +-----------+   |40|Sales |
         staff_v1 v3                                      |50|Mgr   |
ON       v2.id = v3.id                JOIN ANSWER         +---------+
WHERE    v3.name LIKE 'M%'            =================
ORDER BY v1.name                      ID JOB   NAME
        ,v2.job;                      -- ----- --------
                                      30 Clerk Marenghi
                                      30 Mgr   Marenghi
Figure 614, Sample three-table inner join






FROM     clause
JOIN ON  clause
WHERE    clause
GROUP BY and aggregate
HAVING   clause
SELECT   list
ORDER BY clause
FETCH FIRST
Figure 615, Query Processing Sequence






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
LEFT OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       1       = 1                              10 Sanders  -  -
AND      v1.id   = v2.id                          20 Pernal   20 Sales
ORDER BY v1.id                                    30 Marenghi 30 Clerk
        ,v2.job;                                  30 Marenghi 30 Mgr
Figure 616, Sample Views used in Join Examples






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
LEFT OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       1       = 1                              20 Pernal   20 Sales
WHERE    v1.id   = v2.id                          30 Marenghi 30 Clerk
ORDER BY v1.id                                    30 Marenghi 30 Mgr
        ,v2.job;
Figure 617, Sample Views used in Join Examples






STAFF_V1        STAFF_V2                          INNER-JOIN ANSWER
+-----------+   +---------+                       ====================
|ID|NAME    |   |ID|JOB   |    Join on ID         ID NAME     ID JOB
|--|--------|   |--|------|    ==========>        -- -------- -- -----
|10|Sanders |   |20|Sales |                       20 Pernal   20 Sales
|20|Pernal  |   |30|Clerk |                       30 Marenghi 30 Clerk
|30|Marenghi|   |30|Mgr   |                       30 Marenghi 30 Mgr
+-----------+   |40|Sales |
                |50|Mgr   |
                +---------+
Figure 618, Example of Inner Join






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
        ,staff_v2 v2                              ID NAME     ID JOB
WHERE    v1.id = v2.id                            -- -------- -- -----
ORDER BY v1.id                                    20 Pernal   20 Sales
        ,v2.job;                                  30 Marenghi 30 Clerk
                                                  30 Marenghi 30 Mgr
Figure 619, Inner Join SQL (1 of 2)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
INNER JOIN                                        ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            20 Pernal   20 Sales
ORDER BY v1.id                                    30 Marenghi 30 Clerk
        ,v2.job;                                  30 Marenghi 30 Mgr
Figure 620, Inner Join SQL (2 of 2)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
INNER JOIN                                        ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id   = v2.id                          20 Pernal   20 Sales
AND      v2.job <> 'Mgr'                          30 Marenghi 30 Clerk
ORDER BY v1.id
        ,v2.job;
Figure 621, Inner join, using ON check






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
INNER JOIN                                        ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id   = v2.id                          20 Pernal   20 Sales
WHERE    v2.job <> 'Mgr'                          30 Marenghi 30 Clerk
ORDER BY v1.id
        ,v2.job;
Figure 622, Inner join, using WHERE check






STAFF_V1        STAFF_V2                        LEFT-OUTER-JOIN ANSWER
+-----------+   +---------+                     ======================
|ID|NAME    |   |ID|JOB   |                       ID NAME     ID JOB
|--|--------|   |--|------|    =========>         -- -------- -- -----
|10|Sanders |   |20|Sales |                       10 Sanders  -  -
|20|Pernal  |   |30|Clerk |                       20 Pernal   20 Sales
|30|Marenghi|   |30|Mgr   |                       30 Marenghi 30 Clerk
+-----------+   |40|Sales |                       30 Marenghi 30 Mgr
                |50|Mgr   |
                +---------+
Figure 623, Example of Left Outer Join






SELECT   *
FROM     staff_v1 v1
LEFT OUTER JOIN
         staff_v2 v2
ON       v1.id = v2.id
ORDER BY 1,4;
Figure 624, Left Outer Join SQL (1 of 2)






SELECT   v1.*                                   <== This join gets all
        ,v2.*                                       rows in STAFF_V1
FROM     staff_v1 v1                                that match rows
        ,staff_v2 v2                                in STAFF_V2.
WHERE    v1.id = v2.id
UNION  
SELECT   v1.*                                   <== This query gets
        ,CAST(NULL AS SMALLINT) AS id               all the rows in
        ,CAST(NULL AS CHAR(5))  AS job              STAFF_V1 with no
FROM     staff_v1 v1                                matching rows
WHERE    v1.id NOT IN                               in STAFF_V2.
        (SELECT id FROM staff_v2)
ORDER BY 1,4;
Figure 625, Left Outer Join SQL (2 of 2)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
LEFT OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id   = v2.id                          10 Sanders  -  -
AND      v2.job <> 'Mgr'                          20 Pernal   20 Sales
ORDER BY v1.id                                    30 Marenghi 30 Clerk
        ,v2.job;
Figure 626, ON check on table being joined to






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
LEFT OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id   = v2.id                          20 Pernal   20 Sales
WHERE    v2.job <> 'Mgr'                          30 Marenghi 30 Clerk
ORDER BY v1.id
        ,v2.job;
Figure 627, WHERE check on table being joined to (1 of 2)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
LEFT OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id   = v2.id                          10 Sanders  -  -
WHERE   (v2.job <> 'Mgr'                          20 Pernal   20 Sales
   OR    v2.job IS  NULL)                         30 Marenghi 30 Clerk
ORDER BY v1.id
        ,v2.job;
Figure 628, WHERE check on table being joined to (2 of 2)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
LEFT OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id   = v2.id                          10 Sanders  -  -
AND      v1.name > 'N'                            20 Pernal   20 Sales
ORDER BY v1.id                                    30 Marenghi -  -
        ,v2.job;
Figure 629, ON check on table being joined from






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
LEFT OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id   = v2.id                          10 Sanders  -  -
WHERE    v1.name > 'N'                            20 Pernal   20 Sales
ORDER BY v1.id
        ,v2.job;
Figure 630, WHERE check on table being joined from






STAFF_V1        STAFF_V2                       RIGHT-OUTER-JOIN ANSWER
+-----------+   +---------+                    =======================
|ID|NAME    |   |ID|JOB   |                       ID NAME     ID JOB
|--|--------|   |--|------|    =========>         -- -------- -- -----
|10|Sanders |   |20|Sales |                       20 Pernal   20 Sales
|20|Pernal  |   |30|Clerk |                       30 Marenghi 30 Clerk
|30|Marenghi|   |30|Mgr   |                       30 Marenghi 30 Mgr
+-----------+   |40|Sales |                       -  -        40 Sales
                |50|Mgr   |                       -  -        50 Mgr
                +---------+
Figure 631, Example of Right Outer Join






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
RIGHT OUTER JOIN                                  ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            20 Pernal   20 Sales
ORDER BY v2.id                                    30 Marenghi 30 Clerk
        ,v2.job;                                  30 Marenghi 30 Mgr
                                                  -  -        40 Sales
                                                  -  -        50 Mgr
Figure 632, Right Outer Join SQL (1 of 2)






SELECT   v1.*                                     ANSWER
        ,v2.*                                     ====================
FROM     staff_v1 v1                              ID NAME     ID JOB
        ,staff_v2 v2                              -- -------- -- -----
WHERE    v1.id = v2.id                            20 Pernal   20 Sales
UNION                                             30 Marenghi 30 Clerk
SELECT   CAST(NULL AS SMALLINT)   AS id           30 Marenghi 30 Mgr
        ,CAST(NULL AS VARCHAR(9)) AS name         -  -        40 Sales
        ,v2.*                                     -  -        50 Mgr
FROM     staff_v2 v2
WHERE    v2.id NOT IN
        (SELECT id FROM staff_v1)
ORDER BY 3,4;
Figure 633, Right Outer Join SQL (2 of 2)






STAFF_V1        STAFF_V2                        FULL-OUTER-JOIN ANSWER
+-----------+   +---------+                     ======================
|ID|NAME    |   |ID|JOB   |                       ID NAME     ID JOB
|--|--------|   |--|------|    =========>         -- -------- -- -----
|10|Sanders |   |20|Sales |                       10 Sanders  -  -
|20|Pernal  |   |30|Clerk |                       20 Pernal   20 Sales
|30|Marenghi|   |30|Mgr   |                       30 Marenghi 30 Clerk
+-----------+   |40|Sales |                       30 Marenghi 30 Mgr
                |50|Mgr   |                        - -        40 Sales
                +---------+                        - -        50 Mgr
Figure 634, Example of Full Outer Join






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            10 Sanders  -  -
ORDER BY v1.id                                    20 Pernal   20 Sales
        ,v2.id                                    30 Marenghi 30 Clerk
        ,v2.job;                                  30 Marenghi 30 Mgr
                                                   - -        40 Sales
                                                   - -        50 Mgr
Figure 635, Full Outer Join SQL






SELECT   v1.*                                     ANSWER
        ,v2.*                                     ====================
FROM     staff_v1 v1                              ID NAME     ID JOB
        ,staff_v2 v2                              -- -------- -- -----
WHERE    v1.id = v2.id                            10 Sanders  -  -
UNION                                             20 Pernal   20 Sales
SELECT   v1.*                                     30 Marenghi 30 Clerk
        ,CAST(NULL AS SMALLINT) AS id             30 Marenghi 30 Mgr
        ,CAST(NULL AS CHAR(5))  AS job             - -        40 Sales
FROM     staff_v1 v1                               - -        50 Mgr
WHERE    v1.id NOT IN
        (SELECT id FROM staff_v2)
UNION  
SELECT   CAST(NULL AS SMALLINT)   AS id
        ,CAST(NULL AS VARCHAR(9)) AS name
        ,v2.*
FROM     staff_v2 v2
WHERE    v2.id NOT IN
        (SELECT id FROM staff_v1)
ORDER BY 1,3,4;
Figure 636, Full Outer Join SQL






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            10 Sanders   - -
ORDER BY v1.id                                    20 Pernal   20 Sales
        ,v2.id                                    30 Marenghi 30 Clerk
        ,v2.job;                                  30 Marenghi 30 Mgr
                                                   - -        40 Sales
                                                   - -        50 Mgr
Figure 637, Full Outer Join, match on keys






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            10 Sanders   - -
AND      v1.id > 20                               20 Pernal    - -
ORDER BY v1.id                                    30 Marenghi 30 Clerk
        ,v2.id                                    30 Marenghi 30 Mgr
        ,v2.job;                                   - -        20 Sales
                                                   - -        40 Sales
                                                   - -        50 Mgr
Figure 638, Full Outer Join, match on keys > 20






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            10 Sanders   - -
AND      +1 = -1                                  20 Pernal    - -
ORDER BY v1.id                                    30 Marenghi  - -
        ,v2.id                                     - -        20 Sales
        ,v2.job;                                   - -        30 Clerk
                                                   - -        30 Mgr
                                                   - -        40 Sales
                                                   - -        50 Mgr
Figure 639, Full Outer Join, match on keys (no rows match)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       +1 = -1                                  10 Sanders   - -
ORDER BY v1.id                                    20 Pernal    - -
        ,v2.id                                    30 Marenghi  - -
        ,v2.job;                                   - -        20 Sales
                                                   - -        30 Clerk
                                                   - -        30 Mgr
                                                   - -        40 Sales
                                                   - -        50 Mgr
Figure 640, Full Outer Join, don't match on keys (no rows match)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       +1 <> -1                                 10 Sanders  20 Sales
ORDER BY v1.id                                    10 Sanders  30 Clerk
        ,v2.id                                    10 Sanders  30 Mgr
        ,v2.job;                                  10 Sanders  40 Sales
                                                  10 Sanders  50 Mgr
                                                  20 Pernal   20 Sales
STAFF_V1        STAFF_V2                          20 Pernal   30 Clerk
+-----------+   +---------+                       20 Pernal   30 Mgr
|ID|NAME    |   |ID|JOB   |                       20 Pernal   40 Sales
|--|--------|   |--|------|                       20 Pernal   50 Mgr
|10|Sanders |   |20|Sales |                       30 Marenghi 20 Sales
|20|Pernal  |   |30|Clerk |                       30 Marenghi 30 Clerk
|30|Marenghi|   |30|Mgr   |                       30 Marenghi 30 Mgr
+-----------+   |40|Sales |                       30 Marenghi 40 Sales
                |50|Mgr   |                       30 Marenghi 50 Mgr
                +---------+
Figure 641, Full Outer Join, don't match on keys (all rows match)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL JOIN                                         ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            20 Pernal   20 Sales
WHERE    v1.id = v2.id                            30 Marenghi 30 Clerk
ORDER BY 1,3,4;                                   30 Marenghi 30 Mgr
Figure 642, Full Outer Join, turned into an inner join by WHERE






STAFF_V1        STAFF_V2
+-----------+   +---------+                                  ANSWER
|ID|NAME    |   |ID|JOB   |       OUTER-JOIN CRITERIA     ============
|--|--------|   |--|------|       ==================>     ???, DEPENDS
|10|Sanders |   |20|Sales |          V1.ID = V2.ID
|20|Pernal  |   |30|Clerk |          V1.ID < 30
|30|Marenghi|   |30|Mgr   |
+-----------+   |40|Sales |
                |50|Mgr   |
                +---------+
Figure 643, Outer join V1.ID < 30, sample data






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL JOIN                                         ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            10 Sanders  -  -
WHERE    v1.id < 30                               20 Pernal   20 Sales
ORDER BY 1,3,4;
Figure 644, Outer join V1.ID < 30, check applied in WHERE (after join)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL JOIN                                         ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id = v2.id                            10 Sanders  -  -
AND      v1.id < 30                               20 Pernal   20 Sales
ORDER BY 1,3,4;                                   30 Marenghi -  -
                                                  -  -        30 Clerk
                                                  -  -        30 Mgr
                                                  -  -        40 Sales
                                                  -  -        50 Mgr
Figure 645, Outer join V1.ID < 30, check applied in ON (during join)






SELECT   *                                        ANSWER
FROM     (SELECT *                                ====================
          FROM   staff_v1                         ID NAME     ID JOB
          WHERE  id < 30) AS v1                   -- -------- -- -----
FULL OUTER JOIN                                   10 Sanders  -  -
         staff_v2 v2                              20 Pernal   20 Sales
ON       v1.id = v2.id                            -  -        30 Clerk
ORDER BY 1,3,4;                                   -  -        30 Mgr
                                                  -  -        40 Sales
                                                  -  -        50 Mgr
Figure 646, Outer join V1.ID < 30, check applied in WHERE (before join)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id  = v2.id                           10 Sanders  -  -
WHERE    v1.id  < 30                              20 Pernal   20 Sales
   OR    v1.id IS NULL                            -  -        40 Sales
ORDER BY 1,3,4;                                   -  -        50 Mgr
Figure 647, Outer join V1.ID < 30, (gives wrong answer - see text)






SELECT   *                                        ANSWER
FROM     staff_v1 v1                              ====================
FULL OUTER JOIN                                   ID NAME     ID JOB
         staff_v2 v2                              -- -------- -- -----
ON       v1.id  = v2.id                           10 Sanders  -  -
WHERE    v1.id  < 30                              20 Pernal   20 Sales
   OR    v1.id  = v2.id                           30 Marenghi 30 Clerk
   OR    v1.id IS NULL                            30 Marenghi 30 Mgr
ORDER BY 1,3,4;                                   -  -        40 Sales
                                                  -  -        50 Mgr
Figure 648, Outer join V1.ID < 30, (gives wrong answer - see text)






STAFF_V1        STAFF_V2                          CARTESIAN-PRODUCT
+-----------+   +---------+                       ====================
|ID|NAME    |   |ID|JOB   |                       ID NAME     ID JOB
|--|--------|   |--|------|      =========>       -- -------- -- -----
|10|Sanders |   |20|Sales |                       10 Sanders  20 Sales
|20|Pernal  |   |30|Clerk |                       10 Sanders  30 Clerk
|30|Marenghi|   |30|Mgr   |                       10 Sanders  30 Mgr
+-----------+   |40|Sales |                       10 Sanders  40 Sales
                |50|Mgr   |                       10 Sanders  50 Mgr
                +---------+                       20 Pernal   20 Sales
                                                  20 Pernal   30 Clerk
                                                  20 Pernal   30 Mgr
                                                  20 Pernal   40 Sales
                                                  20 Pernal   50 Mgr
                                                  30 Marenghi 20 Sales
                                                  30 Marenghi 30 Clerk
                                                  30 Marenghi 30 Mgr
                                                  30 Marenghi 40 Sales
                                                  30 Marenghi 50 Mgr
Figure 649, Example of Cartesian Product






SELECT   *
FROM     staff_v1 v1
        ,staff_v2 v2
ORDER BY v1.id
        ,v2.id
        ,v2.job;
Figure 650, Cartesian Product SQL (1 of 2)






SELECT   *
FROM     staff_v1 v1
INNER JOIN
         staff_v2 v2
ON       'A' <> 'B'
ORDER BY v1.id
        ,v2.id
        ,v2.job;
Figure 651, Cartesian Product SQL (2 of 2)






SELECT   v2a.id                                            ANSWER
        ,v2a.job                                           ===========
        ,v2b.id                                            ID JOB   ID
FROM     staff_v2 v2a                                      -- ----- --
        ,staff_v2 v2b                                      20 Sales 20
WHERE    v2a.job = v2b.job                                 20 Sales 40
  AND    v2a.id  < 40                                      30 Clerk 30
ORDER BY v2a.id                                            30 Mgr   30
        ,v2b.id;                                           30 Mgr   50
Figure 652, Partial Cartesian Product SQL






SELECT   v2.job                                            ANSWER
        ,COUNT(*) AS #rows                                 ===========
FROM     staff_v1 v1                                       JOB   #ROWS
        ,staff_v2 v2                                       ----- -----
GROUP BY v2.job                                            Clerk     3
ORDER BY #rows                                             Mgr       6
        ,v2.job;                                           Sales     6
Figure 653, Partial Cartesian Product SQL, with GROUP BY






SELECT   COALESCE(v1.id,v2.id) AS id                 ANSWER
        ,COALESCE(v1.name,'?') AS name               =================
        ,v2.job                                      ID NAME     JOB
FROM     staff_v1 v1                                 -- -------- -----
FULL OUTER JOIN                                      10 Sanders  -
         staff_v2 v2                                 20 Pernal   Sales
ON       v1.id = v2.id                               30 Marenghi Clerk
ORDER BY v1.id                                       30 Marenghi Mgr
        ,v2.job;                                     40 ?        Sales
                                                     50 ?        Mgr
Figure 654, Use of COALESCE function in outer join






STAFF_V1        STAFF_V2                           ANSWER
+-----------+   +---------+    NON-MATCHING        ===================
|ID|NAME    |   |ID|JOB   |     OUTER-JOIN         ID NAME    ID JOB
|--|--------|   |--|------|    ===========>        -- ------- -- -----
|10|Sanders |   |20|Sales |                        10 Sanders -  -
|20|Pernal  |   |30|Clerk |                        -  -       40 Sales
|30|Marenghi|   |30|Mgr   |                        -  -       50 Mgr
+-----------+   |40|Sales |
                |50|Mgr   |
                +---------+
Figure 655, Example of outer join, only getting the non-matching rows






SELECT   v1.*                                     <== Get all the rows
        ,CAST(NULL AS SMALLINT) AS id                 in STAFF_V1 that
        ,CAST(NULL AS CHAR(5))  AS job                have no matching
FROM     staff_v1 v1                                  row in STAFF_V2.
WHERE    v1.id NOT IN
        (SELECT id FROM staff_v2)
UNION  
SELECT   CAST(NULL AS SMALLINT)   AS id           <== Get all the rows
        ,CAST(NULL AS VARCHAR(9)) AS name             in STAFF_V2 that
        ,v2.*                                         have no matching
FROM     staff_v2 v2                                  row in STAFF_V1.
WHERE    v2.id NOT IN
        (SELECT id FROM staff_v1)
ORDER BY 1,3,4;
Figure 656, Outer Join SQL, getting only non-matching rows






SELECT   *
FROM     (SELECT v1.*   ,'V1' AS flag   FROM staff_v1 v1) AS v1
FULL OUTER JOIN
         (SELECT v2.*   ,'V2' AS flag   FROM staff_v2 v2) AS v2
ON       v1.id  =  v2.id
WHERE    v1.flag IS NULL                                        ANSWER
   OR    v2.flag IS NULL                 =============================
ORDER BY v1.id                           ID NAME    FLAG ID JOB   FLAG
        ,v2.id                           -- ------- ---- -- ----- ----
        ,v2.job;                         10 Sanders V1    -  -    -
                                         -  -       -    40 Sales V2
                                         -  -       -    50 Mgr   V2
Figure 657, Outer Join SQL, getting only non-matching rows






WITH   
 v1 AS (SELECT v1.*   ,'V1' AS flag   FROM staff_v1 v1)
,v2 AS (SELECT v2.*   ,'V2' AS flag   FROM staff_v2 v2)
SELECT *
FROM   v1 v1                                                    ANSWER
FULL OUTER JOIN                          =============================
       v2 v2                             ID NAME    FLAG ID JOB   FLAG
ON       v1.id  =  v2.id                 -- ------- ---- -- ----- ----
WHERE    v1.flag IS NULL                 10 Sanders V1    -  -    -
   OR    v2.flag IS NULL                 -  -       -    40 Sales V2
ORDER BY v1.id, v2.id, v2.job;           -  -       -    50 Mgr   V2
Figure 658, Outer Join SQL, getting only non-matching rows






SELECT   *                                 STAFF_V1        STAFF_V2
FROM     staff_v1 v1                       +-----------+   +---------+
FULL OUTER JOIN                            |ID|NAME    |   |ID|JOB   |
         staff_v2 v2                       |--|--------|   |--|------|
ON       v1.id  =  v2.id                   |10|Sanders |   |20|Sales |
WHERE    v1.id IS NULL                     |20|Pernal  |   |30|Clerk |
   OR    v2.id IS NULL                     |30|Marenghi|   |30|Mgr   |
ORDER BY v1.id                             +-----------+   |40|Sales |
        ,v2.id                                             |50|Mgr   |
        ,v2.job;                                           +---------+
Figure 659, Outer Join SQL, getting only non-matching rows






STAFF_V1        STAFF_V2                           ANSWER
+-----------+   +---------+    LEFT OUTER JOIN     ===================
|ID|NAME    |   |ID|JOB   |    ==============>     ID NAME    ID JOB
|--|--------|   |--|------|    V1.ID  = V2.ID      -- ------- -- -----
|10|Sanders |   |20|Sales |    V1.ID <> 30         10 Sanders -  -
|20|Pernal  |   |30|Clerk |                        20 Pernal  20 Sales
|30|Marenghi|   |30|Mgr   |
+-----------+   |40|Sales |
                |50|Mgr   |
                +---------+
Figure 660, Left outer join example






SELECT   v1.id                                       ANSWER
        ,v1.name                                     =================
        ,v2.job                                      ID NAME     JOB
FROM     staff_v1 v1                                 -- -------- -----
LEFT OUTER JOIN                                      10 Sanders  -
         staff_v2 v2                                 20 Pernal   Sales
ON       v1.id  = v2.id
WHERE    v1.id <> 30
ORDER BY v1.id ;
Figure 661, Outer Join done in FROM phrase of SQL






SELECT   v1.id                                       ANSWER
        ,v1.name                                     =================
        ,(SELECT  v2.job                             ID NAME     JB
          FROM    staff_v2 v2                        -- -------- -----
          WHERE   v1.id = v2.id) AS jb               10 Sanders  -
FROM     staff_v1 v1                                 20 Pernal   Sales
WHERE    v1.id <> 30
ORDER BY v1.id;
Figure 662, Outer Join done in SELECT phrase of SQL






SELECT   v1.id                                       ANSWER
        ,v1.name                                     =================
        ,(SELECT  v2.job                             ID NAME     JB
          FROM    staff_v2 v2                        -- -------- -----
          WHERE   v1.id = v2.id) AS jb               10 Sanders  -
FROM     staff_v1 v1                                 20 Pernal   Sales
ORDER BY v1.id;                                      
Figure 663, Outer Join done in SELECT phrase of SQL - gets error






SELECT   v1.id                                       ANSWER
        ,v1.name                                     =================
        ,(SELECT  MAX(v2.job)                        ID NAME     JB
          FROM    staff_v2 v2                        -- -------- -----
          WHERE   v1.id = v2.id) AS jb               10 Sanders  -
FROM     staff_v1 v1                                 20 Pernal   Sales
ORDER BY v1.id;                                      30 Marenghi Mgr
Figure 664, Outer Join done in SELECT phrase of SQL - fixed






SELECT   v1.id                                       ANSWER
        ,v1.name                                     =================
        ,MAX(v2.job) AS jb                           ID NAME     JB
FROM     staff_v1 v1                                 -- -------- -----
LEFT OUTER JOIN                                      10 Sanders  -
         staff_v2 v2                                 20 Pernal   Sales
ON       v1.id  = v2.id                              30 Marenghi Mgr
GROUP BY v1.id
        ,v1.name
ORDER BY v1.id ;
Figure 665, Same as prior query - using join and GROUP BY






SELECT   v2.id                                             ANSWER
        ,CASE                                              ===========
            WHEN v2.job <> 'Mgr'                           ID J2
            THEN v2.job                                    -- --------
            ELSE (SELECT v1.name                           20 Sales
                  FROM   staff_v1 v1                       30 Clerk
                  WHERE  v1.id = v2.id)                    30 Marenghi
         END AS j2                                         40 Sales
FROM     staff_v2 v2                                       50 -
ORDER BY v2.id
        ,j2;
Figure 666, Sample Views used in Join Examples






SELECT   v2.id                                    ANSWER
        ,v2.job                                   ====================
        ,(SELECT  v1.name                         ID JOB   NAME     N2
          FROM    staff_v1 v1                     -- ----- -------- --
          WHERE   v2.id = v1.id)                  20 Sales Pernal    6
        ,(SELECT  LENGTH(v1.name) AS n2           30 Clerk Marenghi  8
          FROM    staff_v1 v1                     30 Mgr   Marenghi  8
          WHERE   v2.id = v1.id)                  40 Sales -         -
FROM     staff_v2 v2                              50 Mgr   -         -
ORDER BY v2.id
        ,v2.job;
Figure 667, Outer Join done in SELECT, 2 columns






SELECT   v2.id                                    ANSWER
        ,v2.job                                   ====================
        ,v1.name                                  ID JOB   NAME     N2
        ,LENGTH(v1.name) AS n2                    -- ----- -------- --
FROM     staff_v2 v2                              20 Sales Pernal    6
LEFT OUTER JOIN                                   30 Clerk Marenghi  8
         staff_v1 v1                              30 Mgr   Marenghi  8
ON       v2.id = v1.id                            40 Sales -         -
ORDER BY v2.id                                    50 Mgr   -         -
        ,v2.job;
Figure 668, Outer Join done in FROM, 2 columns






SELECT   v1.id                                      ANSWER
        ,v1.name                                    ==================
        ,(SELECT  SUM(x1.id)                        ID NAME     SUM_ID
          FROM    staff_v1 x1                       -- -------- ------
          WHERE   x1.id <= v1.id                    10 Sanders      10
         )AS sum_id                                 20 Pernal       30
FROM     staff_v1 v1                                30 Marenghi     60
ORDER BY v1.id
        ,v2.job;
Figure 669, Running total, using JOIN in SELECT






SELECT   v1.id                                      ANSWER
        ,v1.name                                    ==================
        ,SUM(id) OVER(ORDER BY id) AS sum_id        ID NAME     SUM_ID
FROM     staff_v1 v1                                -- -------- ------
ORDER BY v1.id;                                     10 Sanders      10
                                                    20 Pernal       30
                                                    30 Marenghi     60
Figure 670, Running total, using OLAP function






STAFF_V1        STAFF_V2                             ANSWER
+-----------+   +---------+                          =================
|ID|NAME    |   |ID|JOB   |    OUTER-JOIN CRITERIA   ID NAME     JOB
|--|--------|   |--|------|    ==================>   -- -------- -----
|10|Sanders |   |20|Sales |    V1.ID     = V2.ID     10 Sanders  -
|20|Pernal  |   |30|Clerk |    V2.JOB LIKE 'S%'      20 Pernal   Sales
|30|Marenghi|   |30|Mgr   |                          30 Marenghi -
+-----------+   |40|Sales |
                |50|Mgr   |
                +---------+
Figure 671, Outer join, with WHERE filter






SELECT   v1.id                                       ANSWER (WRONG)
        ,v1.name                                     =================
        ,v2.job                                      ID NAME     JOB
FROM     staff_v1 v1                                 -- -------- -----
LEFT OUTER JOIN                                      20 Pernal   Sales
         staff_v2 v2
ON       v1.id     = v2.id
WHERE    v2.job LIKE 'S%'
ORDER BY v1.id
        ,v2.job;
Figure 672, Outer Join, WHERE done after - wrong






SELECT   v1.id                                       ANSWER
        ,v1.name                                     =================
        ,v2.job                                      ID NAME     JOB
FROM     staff_v1 v1                                 -- -------- -----
LEFT OUTER JOIN                                      10 Sanders  -
        (SELECT  *                                   20 Pernal   Sales
         FROM    staff_v2                            30 Marenghi -
         WHERE   job LIKE 'S%'
        )AS v2
ON       v1.id = v2.id
ORDER BY v1.id
        ,v2.job;
Figure 673, Outer Join, WHERE done before - correct






SELECT   v1.id                                       ANSWER
        ,v1.name                                     =================
        ,(SELECT v2.job                              ID NAME     JOB
          FROM   staff_v2 v2                         -- -------- -----
          WHERE  v1.id     = v2.id                   10 Sanders  -
            AND  v2.job LIKE 'S%')                   20 Pernal   Sales
FROM     staff_v1 v1                                 30 Marenghi -
ORDER BY v1.id
        ,job;
Figure 674, Outer Join, WHERE done independently - correct






SELECT   eee.empno                          ANSWER
        ,aaa.projno                         ==========================
        ,aaa.actno                          EMPNO  PROJNO ACTNO FORMAT
        ,ppp.photo_format AS format         ------ ------ ----- ------
FROM     employee   eee                     000010 MA2110    10 -
LEFT OUTER JOIN                             000070 -          - -
         emp_act    aaa                     000130 -          - bitmap
ON       eee.empno           =  aaa.empno   000150 MA2112    60 bitmap
AND      aaa.emptime         =  1           000150 MA2112   180 bitmap
AND      aaa.projno       LIKE 'M%1%'       000160 MA2113    60 -
LEFT OUTER JOIN
         emp_photo  ppp
ON       eee.empno           =  ppp.empno
AND      ppp.photo_format LIKE 'b%'
WHERE    eee.lastname     LIKE '%A%'
  AND    eee.empno           < '000170'
  AND    eee.empno          <> '000030'
ORDER BY eee.empno;
Figure 675, Join from Employee to Activity and Photo






SELECT   eee.empno                          ANSWER
        ,aaa.projno                         ==========================
        ,aaa.actno                          EMPNO  PROJNO ACTNO FORMAT
        ,ppp.photo_format AS format         ------ ------ ----- ------
FROM     employee   eee                     000010 MA2110    10 -
LEFT OUTER JOIN                             000070 -          - -
         emp_act    aaa                     000130 -          - -
ON       eee.empno           =  aaa.empno   000150 MA2112    60 bitmap
AND      aaa.emptime         =  1           000150 MA2112   180 bitmap
AND      aaa.projno       LIKE 'M%1%'       000160 MA2113    60 -
LEFT OUTER JOIN
         emp_photo  ppp
ON       aaa.empno           =  ppp.empno
AND      ppp.photo_format LIKE 'b%'
WHERE    eee.lastname     LIKE '%A%'
  AND    eee.empno           < '000170'
  AND    eee.empno          <> '000030'
ORDER BY eee.empno;
Figure 676, Join from Employee to Activity, then from Activity to Photo






SELECT   eee.workdept AS dp#                   ANSWER
        ,eee.empno                             =======================
        ,aaa.projno                            DP# EMPNO  PROJNO STAFF
        ,ppp.prstaff  AS staff                 --- ------ ------ -----
FROM    (SELECT *                              C01 000030 IF1000  2.00
         FROM   employee                       C01 000130 IF1000  2.00
         WHERE  lastname    LIKE '%A%'
           AND  job           <> 'DESIGNER'
           AND  workdept BETWEEN 'B' AND 'E'
        )AS eee
LEFT OUTER JOIN
         emp_act    aaa
ON       aaa.empno       = eee.empno
AND      aaa.emptime    <= 0.5
INNER JOIN
         project    ppp
ON       aaa.projno      = ppp.projno
AND      ppp.projname LIKE '%Q%'
ORDER BY eee.workdept
        ,eee.empno
        ,aaa.projno;
Figure 677, Complex join - wrong






SELECT   eee.workdept AS dp#                   ANSWER
        ,eee.empno                             =======================
        ,xxx.projno                            DP# EMPNO  PROJNO STAFF
        ,xxx.prstaff  AS staff                 --- ------ ------ -----
FROM    (SELECT *                              C01 000030 IF1000  2.00
         FROM   employee                       C01 000130 IF1000  2.00
         WHERE  lastname    LIKE '%A%'         D21 000070 -          -
           AND  job           <> 'DESIGNER'    D21 000240 -          -
           AND  workdept BETWEEN 'B' AND 'E'
        )AS eee
LEFT OUTER JOIN
        (SELECT aaa.empno
               ,aaa.emptime
               ,aaa.projno
               ,ppp.prstaff
         FROM   emp_act    aaa
         INNER JOIN
                project    ppp
         ON     aaa.projno      = ppp.projno
         AND    ppp.projname LIKE '%Q%'
        )AS xxx
ON       xxx.empno     = eee.empno
AND      xxx.emptime  <= 0.5
ORDER BY eee.workdept
        ,eee.empno
        ,xxx.projno;
Figure 678, Complex join - right






SELECT   eee.workdept AS dp#                   ANSWER
        ,eee.empno                             =======================
        ,aaa.projno                            DP# EMPNO  PROJNO STAFF
        ,ppp.prstaff  AS staff                 --- ------ ------ -----
FROM    (SELECT *                              C01 000030 IF1000  2.00
         FROM   employee                       C01 000130 IF1000  2.00
         WHERE  lastname    LIKE '%A%'         D21 000070 -          -
           AND  job           <> 'DESIGNER'    D21 000240 -          -
           AND  workdept BETWEEN 'B' AND 'E'
        )AS eee
LEFT OUTER JOIN
        (       emp_act    aaa
         INNER JOIN
                project    ppp
         ON     aaa.projno      = ppp.projno
         AND    ppp.projname LIKE '%Q%'
        )
ON       aaa.empno     = eee.empno
AND      aaa.emptime  <= 0.5
ORDER BY eee.workdept
        ,eee.empno
        ,aaa.projno;
Figure 679, Complex join - right






CREATE TABLE table1                           TABLE1     TABLE2
(t1a      CHAR(1)    NOT NULL                 +-------+  +-----------+
,t1b      CHAR(2)    NOT NULL                 |T1A|T1B|  |T2A|T2B|T2C|
,PRIMARY KEY(t1a));                           |---|---|  |---|---|---|
COMMIT;                                       |A  |AA |  |A  |A  |A  |
                                              |B  |BB |  |B  |A  | - |
CREATE TABLE table2                           |C  |CC |  +-----------+
(t2a      CHAR(1)    NOT NULL                 +-------+  "-" = null
,t2b      CHAR(1)    NOT NULL
,t2c      CHAR(1));
       
INSERT INTO table1 VALUES ('A','AA'),('B','BB'),('C','CC');
INSERT INTO table2 VALUES ('A','A','A'),('B','A',NULL);
Figure 680, Sample tables used in sub-query examples






Figure 681, Sub-query syntax diagram






SELECT *                                                       ANSWER
FROM   table1                                                  =======
WHERE  t1a =                                                   T1A T1B
      (SELECT t2a                                              --- --
       FROM   table2                                           A   AA
       WHERE  t2a = 'A');
       
                                      SUB-Q   TABLE1     TABLE2
                                      RESLT   +-------+  +-----------+
                                      +---+   |T1A|T1B|  |T2A|T2B|T2C|
                                      |T2A|   |---|---|  |---|---|---|
                                      |---|   |A  |AA |  |A  |A  |A  |
                                      |A  |   |B  |BB |  |B  |A  | - |
                                      +---+   |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 682, No keyword sub-query, works






SELECT *                                                       ANSWER
FROM   table1                                                  =======
WHERE  t1a =                                                   
      (SELECT t2a
       FROM   table2);
                                      SUB-Q   TABLE1     TABLE2
                                      RESLT   +-------+  +-----------+
                                      +---+   |T1A|T1B|  |T2A|T2B|T2C|
                                      |T2A|   |---|---|  |---|---|---|
                                      |---|   |A  |AA |  |A  |A  |A  |
                                      |A  |   |B  |BB |  |B  |A  | - |
                                      |B  |   |C  |CC |  +-----------+
                                      +---+   +-------+  "-" = null
Figure 683, No keyword sub-query, fails






SELECT *                     ANSWER    SUB-Q  TABLE1     TABLE2
FROM   table1                =======   RESLT  +-------+  +-----------+
WHERE  t1a > ANY             T1A T1B   +---+  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2a            --- --    |T2A|  |---|---|  |---|---|---|
       FROM   table2);       B   BB    |---|  |A  |AA |  |A  |A  |A  |
                             C   CC    |A  |  |B  |BB |  |B  |A  | - |
                                       |B  |  |C  |CC |  +-----------+
                                       +---+  +-------+  "-" = null
Figure 684, ANY sub-query






SUB-QUERY CHECK      EQUIVALENT COLUMN FUNCTION
================     ============================
> ANY(sub-qurey)     > MINIMUM(sub-query results)
< ANY(sub-query)     < MAXIMUM(sub-query results)
       
> ALL(sub-query)     > MAXIMUM(sub-query results)
< ALL(sub-query)     < MINIMUM(sub-query results)
Figure 685, ANY and ALL vs. column functions






SELECT *                                              ANSWER     SUB-Q
FROM   table1                                         =======    RESLT
WHERE  t1a = ALL                                      T1A T1B    +---+
      (SELECT t2b                                     --- --     |T2B|
       FROM   table2                                  A   AA     |---|
       WHERE  t2b >= 'A');                                       |A  |
                                                                 |A  |
                                                                 +---+
Figure 686, ALL sub-query, with non-empty sub-query result






SELECT *                                              ANSWER     SUB-Q
FROM   table1                                         =======    RESLT
WHERE  t1a = ALL                                      T1A T1B    +---+
      (SELECT t2b                                     --- --     |T2B|
       FROM   table2                                  A   AA     |---|
       WHERE  t2b >= 'X');                            B   BB     +---+
                                                      C   CC
Figure 687, ALL sub-query, with empty sub-query result






SELECT *                                                        ANSWER
FROM   table1                                                   ======
WHERE  t1a = ALL                                                0 rows
      (SELECT t2b
       FROM   table2            SQ-#1  SQ-#2  TABLE1     TABLE2
       WHERE  t2b >= 'X')       RESLT  RESLT  +-------+  +-----------+
  AND  0 <>                     +---+  +---+  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT COUNT(*)          |T2B|  |(*)|  |---|---|  |---|---|---|
       FROM   table2            |---|  |---|  |A  |AA |  |A  |A  |A  |
       WHERE  t2b >= 'X');      +---+  |0  |  |B  |BB |  |B  |A  | - |
                                       +---+  |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 688, ALL sub-query, with extra check for empty set






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  EXISTS                        T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT *                      --- --   |---|---|  |---|---|---|
       FROM   table2);               A   AA   |A  |AA |  |A  |A  |A  |
                                     B   BB   |B  |BB |  |B  |A  | - |
                                     C   CC   |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 689, EXISTS sub-query, always returns a match






SELECT *                                                        ANSWER
FROM   table1                                                   ======
WHERE  EXISTS                                                   0 rows
      (SELECT *
       FROM   table2
       WHERE  t2b >= 'X');
Figure 690, EXISTS sub-query, always returns a non-match






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  EXISTS                        T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT COUNT(*)               --- --   |---|---|  |---|---|---|
       FROM   table2                 A   AA   |A  |AA |  |A  |A  |A  |
       WHERE  t2b = 'X');            B   BB   |B  |BB |  |B  |A  | - |
                                     C   CC   |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 691, EXISTS sub-query, always returns a match






SELECT *                             ANSWERS  TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  NOT EXISTS                    T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT *                      --- ---  |---|---|  |---|---|---|
       FROM   table2                 A   AA   |A  |AA |  |A  |A  |A  |
       WHERE  t2c >= 'A'                      |B  |BB |  |B  |A  | - |
         AND  t2c <> t1a);                    |C  |CC |  +-----------+
                                              +-------+  "-" = null
       
SELECT *
FROM   table1
WHERE  t1a = ALL
      (SELECT t2c
       FROM   table2
       WHERE  t2c >= 'A');
Figure 692, NOT EXISTS vs. ALL, ignore nulls, find match






SELECT *                             ANSWERS  TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  NOT EXISTS                    T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT *                      --- ---  |---|---|  |---|---|---|
       FROM   table2                 A   AA   |A  |AA |  |A  |A  |A  |
       WHERE  t2c >= 'X'             B   BB   |B  |BB |  |B  |A  | - |
         AND  t2c <> t1a);           C   CC   |C  |CC |  +-----------+
                                              +-------+  "-" = null
       
SELECT *
FROM   table1
WHERE  t1a = ALL
      (SELECT t2c
       FROM   table2
       WHERE  t2c >= 'X');
Figure 693, NOT EXISTS vs. ALL, ignore nulls, no match






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  NOT EXISTS                    T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT *                      --- ---  |---|---|  |---|---|---|
       FROM   table2                 A   AA   |A  |AA |  |A  |A  |A  |
       WHERE  t2c <> t1a);                    |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
                                              +-------+  "-" = null
SELECT *                             ANSWER
FROM   table1                        =======
WHERE  t1a = ALL                     no rows
      (SELECT t2c
       FROM   table2);
Figure 694, NOT EXISTS vs. ALL, process nulls






SELECT *                 SELECT *                  SELECT *
FROM   table2            FROM   table2             FROM   table2
WHERE  t2c <> 'A';       WHERE  t2c <> 'B';        WHERE  t2c <> 'C';
       
ANSWER                   ANSWER                    ANSWER
===========              ===========               ===========
T2A T2B T2C              T2A T2B T2C               T2A T2B T2C
--- --- ---              --- --- ---               --- --- ---
no rows                  A   A   A                 A   A   A
Figure 695, List of values in T2C <> T1A value






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  NOT EXISTS                    no rows  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT *                               |---|---|  |---|---|---|
       FROM   table2                          |A  |AA |  |A  |A  |A  |
       WHERE  t2c <> t1a                      |B  |BB |  |B  |A  | - |
          OR  t2c IS NULL);                   |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 696, NOT EXISTS - same as ALL






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  t1a IN                        T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2a                    --- --   |---|---|  |---|---|---|
       FROM   table2);               A   AA   |A  |AA |  |A  |A  |A  |
                                     B   BB   |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 697, IN sub-query example, two matches






SELECT *                                                        ANSWER
FROM   table1                                                   ======
WHERE  t1a IN                                                   0 rows
      (SELECT t2a
       FROM   table2
       WHERE  t2a >= 'X');
Figure 698, IN sub-query example, no matches






SELECT *                                   ANSWERS       TABLE2
FROM   table2                              ===========   +-----------+
WHERE  t2c IN                              T2A T2B T2C   |T2A|T2B|T2C|
      (SELECT t2c                          --- --- ---   |---|---|---|
       FROM   table2);                     A   A   A     |A  |A  |A  |
                                                         |B  |A  | - |
SELECT *                                                 +-----------+
FROM   table2                                            "-" = null
WHERE  t2c = ANY
      (SELECT t2c
       FROM   table2);
Figure 699, IN and = ANY sub-query examples, with nulls






SELECT *                              ANSWER  TABLE1     TABLE2
FROM   table1                         ======  +-------+  +-----------+
WHERE  t1a NOT IN                     0 rows  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2c                             |---|---|  |---|---|---|
       FROM   table2);                        |A  |AA |  |A  |A  |A  |
                                              |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 700, NOT IN sub-query example, no matches






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  t1a NOT IN                    T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2c                    --- --   |---|---|  |---|---|---|
       FROM   table2                 B   BB   |A  |AA |  |A  |A  |A  |
       WHERE  t2c IS NOT NULL);      C   CC   |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 701, NOT IN sub-query example, matches






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  NOT EXISTS                    T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT *                      --- --   |---|---|  |---|---|---|
       FROM   table2                 B   BB   |A  |AA |  |A  |A  |A  |
       WHERE  t1a = t2c);            C   CC   |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 702, NOT EXISTS sub-query example, matches






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  t1a IN                        T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2a                    --- --   |---|---|  |---|---|---|
       FROM   table2);               A   AA   |A  |AA |  |A  |A  |A  |
                                     B   BB   |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 703, Uncorrelated sub-query






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  t1a IN                        T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2a                    --- --   |---|---|  |---|---|---|
       FROM   table2                 A   AA   |A  |AA |  |A  |A  |A  |
       WHERE  t1a = t2a);            B   BB   |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 704, Correlated sub-query






SELECT *                                    ANSWER       TABLE2
FROM   table2 aa                            ===========  +-----------+
WHERE  EXISTS                               T2A T2B T2C  |T2A|T2B|T2C|
      (SELECT *                             --- --- ---  |---|---|---|
       FROM   table2 bb                     A   A   A    |A  |A  |A  |
       WHERE  aa.t2a = bb.t2b);                          |B  |A  | - |
                                                         +-----------+
                                                         "-" = null
Figure 705,Correlated sub-query, with correlation names






SELECT *                              ANSWER  TABLE1     TABLE2
FROM   table1                         ======  +-------+  +-----------+
WHERE (t1a,t1b) IN                    0 rows  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2a, t2b                        |---|---|  |---|---|---|
       FROM   table2);                        |A  |AA |  |A  |A  |A  |
                                              |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
                                              +-------+  "-" = null
SELECT *                          ANSWER
FROM   table1                     ======
WHERE  EXISTS                     0 rows
      (SELECT   *
       FROM     table2
       WHERE    t1a = t2a
         AND    t1b = t2b);
Figure 706, Multi-field sub-queries, equal checks






SELECT *                             ANSWER   TABLE1     TABLE2
FROM   table1                        =======  +-------+  +-----------+
WHERE  EXISTS                        T1A T1B  |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT   *                    --- --   |---|---|  |---|---|---|
       FROM     table2               A   AA   |A  |AA |  |A  |A  |A  |
       WHERE    t1a  = t2a           B   BB   |B  |BB |  |B  |A  | - |
         AND    t1b >= t2b);                  |C  |CC |  +-----------+
                                              +-------+  "-" = null
Figure 707, Multi-field sub-query, with non-equal check






SELECT empno                                 ANSWER
      ,lastname                              =========================
      ,salary                                EMPNO  LASTNAME  SALARY
FROM   employee                              ------ --------- --------
WHERE  salary >                              000010 HAAS      52750.00
      (SELECT MAX(salary)                    000110 LUCCHESSI 46500.00
       FROM   employee
       WHERE  empno NOT IN
             (SELECT empno
              FROM   emp_act
              WHERE  projno LIKE 'MA%'))
ORDER BY 1;
Figure 708, Nested Sub-Queries






SELECT   COUNT(*)     AS #rows                           ANSWER
        ,MAX(deptno)  AS maxdpt                          =============
FROM     department                                      #ROWS MAXDEPT
WHERE    deptname LIKE 'Z%'                              ----- -------
ORDER BY 1;                                                  0   null
Figure 709, Getting a null value from a not null field






SELECT *                                      TABLE1     TABLE2
FROM   table1 t1                              +-------+  +-----------+
WHERE  0 =                                    |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT COUNT(*)                        |---|---|  |---|---|---|
       FROM   table2 t2                       |A  |AA |  |A  |A  |A  |
       WHERE  t1.t1a = t2.t2c);               |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
SELECT *                                      +-------+  "-" = null
FROM   table1 t1
WHERE  NOT EXISTS
      (SELECT *                                                ANSWER
       FROM   table2 t2                                        =======
       WHERE  t1.t1a = t2.t2c);                                T1A T1B
                                                               --- ---
SELECT *                                                       B   BB
FROM   table1                                                  C   CC
WHERE  t1a NOT IN
      (SELECT t2c
       FROM   table2
       WHERE  t2c IS NOT NULL);
Figure 710, Sub-queries, true if none match






SELECT t1.*                                                    ANSWER
FROM   table1 t1                                               =======
LEFT OUTER JOIN                                                T1A T1B
       table2 t2                                               --- ---
ON     t1.t1a  = t2.t2c                                        B   BB
WHERE  t2.t2c IS NULL;                                         C   CC
Figure 711, Outer join, true if none match






SELECT *                                      TABLE1     TABLE2
FROM   table1 t1                              +-------+  +-----------+
WHERE  EXISTS                                 |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT *                               |---|---|  |---|---|---|
       FROM   table2 t2                       |A  |AA |  |A  |A  |A  |
       WHERE  t1.t1a = t2.t2c);               |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
SELECT *                                      +-------+  "-" = null
FROM   table1 t1
WHERE  1 <=
      (SELECT COUNT(*)                                         ANSWER
       FROM   table2 t2                                        =======
       WHERE  t1.t1a = t2.t2c);                                T1A T1B
                                                               --- ---
SELECT *                                                       A   AA
FROM   table1
WHERE  t1a = ANY
      (SELECT t2c
       FROM   table2);
       
SELECT *
FROM   table1
WHERE  t1a = SOME
      (SELECT t2c
       FROM   table2);
       
SELECT *
FROM   table1
WHERE  t1a IN
      (SELECT t2c
       FROM   table2);
Figure 712, Sub-queries, true if any match






WITH t2 AS                                    TABLE1     TABLE2
(SELECT DISTINCT t2c                          +-------+  +-----------+
 FROM   table2                                |T1A|T1B|  |T2A|T2B|T2C|
)                                             |---|---|  |---|---|---|
SELECT t1.*                                   |A  |AA |  |A  |A  |A  |
FROM   table1 t1                              |B  |BB |  |B  |A  | - |
      ,t2                                     |C  |CC |  +-----------+
WHERE  t1.t1a = t2.t2c;                       +-------+  "-" = null
       
SELECT t1.*
FROM   table1 t1                                               ANSWER
      ,(SELECT DISTINCT t2c                                    =======
        FROM   table2                                          T1A T1B
       )AS t2                                                  --- ---
WHERE   t1.t1a = t2.t2c;                                       A   AA
       
SELECT t1.*
FROM   table1 t1
INNER JOIN
       (SELECT   DISTINCT t2c
        FROM     table2
       )AS t2
ON      t1.t1a = t2.t2c;
Figure 713, Joins, true if any match






SELECT *                                      TABLE1     TABLE2
FROM   table1 t1                              +-------+  +-----------+
WHERE  10 =                                   |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT   COUNT(*)                      |---|---|  |---|---|---|
       FROM     table2 t2                     |A  |AA |  |A  |A  |A  |
       WHERE    t1.t1a = t2.t2b);             |B  |BB |  |B  |A  | - |
                                              |C  |CC |  +-----------+
SELECT *                                      +-------+  "-" = null
FROM   table1
WHERE  EXISTS
      (SELECT   t2b                                             ANSWER
       FROM     table2                                          ======
       WHERE    t1a = t2b                                       0 rows
       GROUP BY t2b
       HAVING   COUNT(*) = 10);
       
SELECT *
FROM   table1
WHERE  t1a IN
      (SELECT   t2b
       FROM     table2
       GROUP BY t2b
       HAVING   COUNT(*) = 10);
Figure 714, Sub-queries, true if ten match (1 of 2)






SELECT *                                                        ANSWER
FROM   table1                                                   ======
WHERE (t1a,10) IN                                               0 rows
      (SELECT   t2b, COUNT(*)
       FROM     table2
       GROUP BY t2b);
Figure 715, Sub-queries, true if ten match (2 of 2)






WITH t2 AS                                    TABLE1     TABLE2
  (SELECT   t2b                               +-------+  +-----------+
   FROM     table2                            |T1A|T1B|  |T2A|T2B|T2C|
   GROUP BY t2b                               |---|---|  |---|---|---|
   HAVING   COUNT(*) = 10                     |A  |AA |  |A  |A  |A  |
  )                                           |B  |BB |  |B  |A  | - |
SELECT t1.*                                   |C  |CC |  +-----------+
FROM   table1 t1                              +-------+  "-" = null
      ,t2
WHERE  t1.t1a = t2.t2b;
       
                                                                ANSWER
SELECT t1.*                                                     ======
FROM   table1 t1                                                0 rows
      ,(SELECT   t2b
        FROM     table2
        GROUP BY t2b
        HAVING   COUNT(*) = 10
       )AS t2
WHERE   t1.t1a = t2.t2b;
       
       
SELECT t1.*
FROM   table1 t1
INNER JOIN
       (SELECT   t2b
        FROM     table2
        GROUP BY t2b
        HAVING   COUNT(*) = 10
       )AS t2
ON      t1.t1a = t2.t2b;
Figure 716, Joins, true if ten match






SELECT *                                      TABLE1     TABLE2
FROM   table1                                 +-------+  +-----------+
WHERE  t1a = ALL                              |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2b                             |---|---|  |---|---|---|
       FROM   table2);                        |A  |AA |  |A  |A  |A  |
                                              |B  |BB |  |B  |A  | - |
SELECT *                                      |C  |CC |  +-----------+
FROM   table1                                 +-------+  "-" = null
WHERE  NOT EXISTS
      (SELECT *                                                ANSWER
       FROM   table2                                           =======
       WHERE  t1a <> t2b);                                     T1A T1B
                                                               --- ---
                                                               A   AA
Figure 717, Sub-queries, true if all match, find rows






SELECT *                                                       ANSWER
FROM   table1                                                  =======
WHERE  t1a = ALL                                               T1A T1B
      (SELECT t2b                                              --- ---
       FROM   table2                                           A   AA
       WHERE  t2b >= 'X');                                     B   BB
                                                               C   CC
SELECT *
FROM   table1
WHERE  NOT EXISTS
      (SELECT *
       FROM   table2
       WHERE  t1a <> t2b
         AND  t2b >= 'X');
Figure 718, Sub-queries, true if all match, empty set






SELECT *                                      TABLE1     TABLE2
FROM   table1                                 +-------+  +-----------+
WHERE  t1a = ALL                              |T1A|T1B|  |T2A|T2B|T2C|
      (SELECT t2b                             |---|---|  |---|---|---|
       FROM   table2                          |A  |AA |  |A  |A  |A  |
       WHERE  t2b >= 'X')                     |B  |BB |  |B  |A  | - |
  AND  0 <>                                   |C  |CC |  +-----------+
      (SELECT COUNT(*)                        +-------+  "-" = null
       FROM   table2
       WHERE  t2b >= 'X');                                      ANSWER
                                                                ======
SELECT *                                                        0 rows
FROM   table1
WHERE  t1a IN
      (SELECT MAX(t2b)
       FROM   table2
       WHERE  t2b >= 'X'
       HAVING COUNT(DISTINCT t2b) = 1);
Figure 719, Sub-queries, true if all match, and at least one value found






           R1     R1      R1         R1          R1      R1      R1
           UNION  UNION   INTERSECT  INTERSECT   EXCEPT  EXCEPT  MINUS
           R2     ALL     R2         ALL         R2      ALL     R2
R1  R2            R2                 R2                  R2
--  --     -----  -----   ---------  -----       ------  ------  -----
A   A      A      A       A          A           E       A       E
A   A      B      A       B          A                   C
A   B      C      A       C          B                   C
B   B      D      A                  B                   E
B   B      E      A                  C
C   C             B
C   D             B
C                 B
E                 B
                  B
                  C
                  C
                  C
                  C
                  D
                  E
Figure 720, Examples of Union, Except, and Intersect






Figure 721, Union, Except, and Intersect syntax






CREATE VIEW R1 (R1)
  AS VALUES ('A'),('A'),('A'),('B'),('B'),('C'),('C'),('C'),('E');
CREATE VIEW R2 (R2)
  AS VALUES ('A'),('A'),('B'),('B'),('B'),('C'),('D');          ANSWER
                                                                ======
SELECT   R1                                                     R1  R2
FROM     R1                                                     --  --
ORDER BY R1;                                                    A   A
                                                                A   A
SELECT   R2                                                     A   B
FROM     R2                                                     B   B
ORDER BY R2;                                                    B   B
                                                                C   C
                                                                C   D
                                                                C
                                                                E
Figure 722, Query sample views






SELECT   R1                                  R1  R2   UNION  UNION ALL
FROM     R1                                  --  --   =====  =========
UNION                                        A   A    A      A
SELECT   R2                                  A   A    B      A
FROM     R2                                  A   B    C      A
ORDER BY 1;                                  B   B    D      A
                                             B   B    E      A
                                             C   C           B
SELECT   R1                                  C   D           B
FROM     R1                                  C               B
UNION ALL                                    E               B
SELECT   R2                                                  B
FROM     R2                                                  C
ORDER BY 1;                                                  C
                                                             C
                                                             C
                                                             D
                                                             E
Figure 723, Union and Union All SQL






SELECT   R1                          R1  R2   INTERSECT  INTERSECT ALL
FROM     R1                          --  --   =========  =============
INTERSECT                            A   A    A          A
SELECT   R2                          A   A    B          A
FROM     R2                          A   B    C          B
ORDER BY 1;                          B   B               B
                                     B   B               C
SELECT   R1                          C   C
FROM     R1                          C   D
INTERSECT ALL                        C
SELECT   R2                          E
FROM     R2
ORDER BY 1;
Figure 724, Intersect and Intersect All SQL






SELECT   R1                                         R1      R1
FROM     R1                                         EXCEPT  EXCEPT ALL
EXCEPT                                     R1  R2   R2      R2
SELECT   R2                                --  --   =====   ==========
FROM     R2                                A   A    E       A
ORDER BY 1;                                A   A            C
                                           A   B            C
SELECT   R1                                B   B            E
FROM     R1                                B   B
EXCEPT  ALL                                C   C
SELECT   R2                                C   D
FROM     R2                                C
ORDER BY 1;                                E
Figure 725, Except and Except All SQL (R1 on top)






SELECT   R2                                         R2      R2
FROM     R2                                         EXCEPT  EXCEPT ALL
EXCEPT                                     R1  R2   R1      R1
SELECT   R1                                --  --   =====   ==========
FROM     R1                                A   A    D       B
ORDER BY 1;                                A   A            D
                                           A   B
SELECT   R2                                B   B
FROM     R2                                B   B
EXCEPT  ALL                                C   C
SELECT   R1                                C   D
FROM     R1                                C
ORDER BY 1;                                E
Figure 726, Except and Except All SQL (R2 on top)






SELECT   R1      (SELECT   R1        SELECT   R1                R1  R2
FROM     R1       FROM     R1        FROM     R1                --  --
UNION             UNION              UNION                      A   A
SELECT   R2       SELECT   R2       (SELECT   R2                A   A
FROM     R2       FROM     R2        FROM     R2                A   B
EXCEPT           )EXCEPT             EXCEPT                     B   B
SELECT   R2       SELECT   R2        SELECT   R2                B   B
FROM     R2       FROM     R2        FROM     R2                C   C
ORDER BY 1;       ORDER BY 1;       )ORDER BY 1;                C   D
                                                                C
                                                                E
ANSWER            ANSWER             ANSWER
======            ======             ======
E                 E                  A
                                     B
                                     C
                                     E
Figure 727, Use of parenthesis in Union






CREATE TABLE sales_data_2002
(sales_date         DATE        NOT NULL
,daily_seq#         INTEGER     NOT NULL
,cust_id            INTEGER     NOT NULL
,amount             DEC(10,2)   NOT NULL
,invoice#           INTEGER     NOT NULL
,sales_rep          CHAR(10)    NOT NULL
,CONSTRAINT C CHECK (YEAR(sales_date) = 2002)
,PRIMARY KEY (sales_date, daily_seq#));
       
CREATE TABLE sales_data_2003
(sales_date         DATE        NOT NULL
,daily_seq#         INTEGER     NOT NULL
,cust_id            INTEGER     NOT NULL
,amount             DEC(10,2)   NOT NULL
,invoice#           INTEGER     NOT NULL
,sales_rep          CHAR(10)    NOT NULL
,CONSTRAINT C CHECK (YEAR(sales_date) = 2003)
,PRIMARY KEY (sales_date, daily_seq#));
       
CREATE VIEW sales_data AS
SELECT *
FROM   sales_data_2002
UNION ALL
SELECT *
FROM   sales_data_2003;
Figure 728, Define view to combine yearly tables






INSERT INTO sales_data VALUES ('2002-11-22',1,123,100.10,996,'SUE')
                             ,('2002-11-22',2,123,100.10,997,'JOHN')
                             ,('2003-01-01',1,123,100.10,998,'FRED')
                             ,('2003-01-01',2,123,100.10,999,'FRED');
       
UPDATE sales_data
SET    amount = amount / 2
WHERE  sales_rep = 'JOHN';
       
DELETE 
FROM   sales_data
WHERE  sales_date = '2003-01-01'
  AND  daily_seq# =  2;
Figure 729, Insert, update, and delete using view






SALES_DATE  DAILY_SEQ#  CUST_ID  AMOUNT  INVOICE#  SALES_REP
----------  ----------  -------  ------  --------  ---------
01/01/2003           1      123  100.10       998  FRED
11/22/2002           1      123  100.10       996  SUE
11/22/2002           2      123   50.05       997  JOHN
Figure 730, View contents after insert, update, delete






CREATE TABLE staff_summary AS
  (SELECT   dept
           ,COUNT(*) AS count_rows
           ,SUM(id)  AS sum_id
   FROM     staff
   GROUP BY dept)
DATA INITIALLY DEFERRED REFRESH IMMEDIATE;
Figure 731, Sample materialized query table DDL






ORIGINAL QUERY                       OPTIMIZED QUERY
==============                       =================================
SELECT   dept                        SELECT  Q1.dept AS "dept"
        ,AVG(id)                            ,Q1.sum_id / Q1.count_rows
FROM     staff                       FROM    staff_summary AS Q1
GROUP BY dept
Figure 732, Original and optimized queries






Figure 733, Materialized query table DDL, syntax diagram






MATERIALIZED QUERY TABLE         ALLOWABLE ACTIONS ON TABLE
==========================       =====================================
REFRESH      MAINTAINED BY       REFRESH TABLE    INSERT/UPDATE/DELETE
=========    =============       =============    ====================
DEFERRED     SYSTEM              yes              no
             USER                no               yes
IMMEDIATE    SYSTEM              yes              no
Figure 734, Materialized query table options vs. allowable actions






UPDATE DATABASE CONFIGURATION USING dft_refresh_age ANY;
Figure 735, Changing default refresh age for database






Figure 736, Set refresh age command, syntax






SET CURRENT REFRESH AGE   0;
SET CURRENT REFRESH AGE = ANY;
SET CURRENT REFRESH AGE = 99999999999999;
Figure 737, Set refresh age command, examples






UPDATE DATABASE CONFIGURATION USING dft_refresh_age ANY;
Figure 738, Changing default maintained type for database






Figure 739,Set maintained type command, syntax






SET CURRENT MAINTAINED       TYPES                  = ALL;
SET CURRENT MAINTAINED TABLE TYPES                  = SYSTEM;
SET CURRENT MAINTAINED TABLE TYPES FOR OPTIMIZATION = USER, SYSTEM;
Figure 740, Set maintained type command, examples






UPDATE DATABASE CONFIGURATION USING DFT_QUERYOPT 5;
Figure 741, Changing default maintained type for database






Figure 742,Set maintained type command, syntax






SET CURRENT QUERY OPTIMIZATION = 9;
figure 743, Set query optimization, example






   MQT DEFINITION                DATABASE/APPLICATION STATUS       DB2
==========================   ===================================   USE
REFRESH     MAINTAINED-BY    REFRESH-AGE   MAINTAINED-TYPE         MQT
=========   ==============   ===========   =====================   ===
IMMEDIATE   SYSTEM           -             -                       Yes
DEFERRED    SYSETM           ANY           ALL or SYSTEM           Yes
DEFERRED    USER             ANY           ALL or USER             Yes
DEFERRED    FEDERATED-TOOL   ANY           ALL or FEDERATED-TOOL   Yes
Figure 744, When DB2 will consider using a materialized query table






SELECT  CURRENT REFRESH AGE           AS age_ts
       ,CURRENT TIMESTAMP             AS current_ts
       ,CURRENT QUERY OPTIMIZATION    AS q_opt
FROM    sysibm.sysdummy1;
Figure 745, Selecting special registers






CREATE TABLE staff_names AS
  (SELECT   dept
           ,COUNT(*)          AS count_rows
           ,SUM(salary)       AS sum_salary
           ,AVG(salary)       AS avg_salary
           ,MAX(salary)       AS max_salary
           ,MIN(salary)       AS min_salary
           ,STDDEV(salary)    AS std_salary
           ,VARIANCE(salary)  AS var_salary
           ,CURRENT TIMESTAMP AS last_change
   FROM     staff
   WHERE    TRANSLATE(name) LIKE '%A%'
     AND    salary             > 10000
   GROUP BY dept
   HAVING   COUNT(*) = 1
)DATA INITIALLY DEFERRED REFRESH DEFERRED;
Figure 746, Refresh deferred materialized query table DDL






CREATE TABLE emp_summary AS
  (SELECT   emp.workdept
           ,COUNT(*)           AS num_rows
           ,COUNT(emp.salary)  AS num_salary
           ,SUM(emp.salary)    AS sum_salary
           ,COUNT(emp.comm)    AS num_comm
           ,SUM(emp.comm)      AS sum_comm
   FROM     employee emp
   GROUP BY emp.workdept
)DATA INITIALLY DEFERRED REFRESH IMMEDIATE;
Figure 747, Refresh immediate materialized query table DDL






SELECT   emp.workdept
        ,DEC(SUM(emp.salary),8,2)   AS sum_sal
        ,DEC(AVG(emp.salary),7,2)   AS avg_sal
        ,SMALLINT(COUNT(emp.comm))  AS #comms
        ,SMALLINT(COUNT(*))         AS #emps
FROM     employee emp
WHERE    emp.workdept    > 'C'
GROUP BY emp.workdept
HAVING   COUNT(*)       <> 5
   AND   SUM(emp.salary) > 50000
ORDER BY sum_sal DESC;
Figure 748, Query that uses materialized query table (1 of 3)






SELECT   emp.workdept
        ,COUNT(*)      AS #rows
FROM     employee emp
WHERE    emp.workdept IN
        (SELECT deptno
         FROM   department
         WHERE  deptname LIKE '%S%')
GROUP BY emp.workdept
HAVING   SUM(salary) > 50000;
Figure 749, Query that uses materialized query table (2 of 3)






SELECT   #emps
        ,DEC(SUM(sum_sal),9,2)   AS sal_sal
        ,SMALLINT(COUNT(*))      AS #depts
FROM    (SELECT   emp.workdept
                 ,DEC(SUM(emp.salary),8,2)   AS sum_sal
                 ,MAX(emp.salary)            AS max_sal
                 ,SMALLINT(COUNT(*))         AS #emps
         FROM     employee emp
         GROUP BY emp.workdept
        )AS XXX
GROUP BY #emps
HAVING   COUNT(*) > 1
ORDER BY #emps
FETCH FIRST 3 ROWS ONLY
OPTIMIZE FOR 3 ROWS;
Figure 750, Query that uses materialized query table (3 of 3)






CREATE TABLE staff_all
(id        SMALLINT        NOT NULL
,name      VARCHAR(9)      NOT NULL
,job       CHAR(5)
,salary    DECIMAL(7,2)
,PRIMARY KEY(id));
Figure 751, Create source table






CREATE TABLE staff_all_dup AS
  (SELECT  *
   FROM    staff_all)
DATA INITIALLY DEFERRED REFRESH IMMEDIATE;
Figure 752, Create duplicate data table






CREATE TABLE staff_all_dup_some AS
  (SELECT  *
   FROM    staff_all
   WHERE   id < 30)
DATA INITIALLY DEFERRED REFRESH IMMEDIATE;
Figure 753, Create table - duplicate certain rows only






CREATE TABLE staff_to_fire
(id        SMALLINT        NOT NULL
,name      VARCHAR(9)      NOT NULL
,dept      SMALLINT
,PRIMARY KEY(id));
Figure 754, Create source table






CREATE TABLE staff_combo AS
  (SELECT  aaa.id      AS id1
          ,aaa.job     AS job
          ,fff.id      as id2
          ,fff.dept    AS dept
   FROM    staff_all     aaa
          ,staff_to_fire fff
   WHERE   aaa.id = fff.id)
DATA INITIALLY DEFERRED REFRESH IMMEDIATE;
Figure 755, Materialized query table on join






SELECT   emp.workdept
        ,DEC(SUM(emp.salary),8,2)   AS sum_sal
        ,MAX(emp.salary)            AS max_sal
FROM     employee emp
GROUP BY emp.workdept;
Figure 756, Query that doesn't use materialized query table (1 of 2)






SELECT   emp.workdept
        ,DEC(SUM(emp.salary),8,2)   AS sum_sal
        ,COUNT(DISTINCT salary)     AS #salaries
FROM     employee emp
GROUP BY emp.workdept;
Figure 757, Query that doesn't use materialized query table (2 of 2)






REFRESH TABLE emp_summary;
COMMIT;
       
SET INTEGRITY FOR emp_summary iMMEDIATE CHECKED;
COMMIT;
Figure 758, Materialized query table refresh commands






CREATE TABLE dept_emp_summary AS
  (SELECT   emp.workdept
           ,dpt.deptname
           ,COUNT(*)           AS num_rows
           ,COUNT(emp.salary)  AS num_salary
           ,SUM(emp.salary)    AS sum_salary
           ,COUNT(emp.comm)    AS num_comm
           ,SUM(emp.comm)      AS sum_comm
   FROM     employee   emp
           ,department dpt
   WHERE    dpt.deptno = emp.workdept
   GROUP BY emp.workdept
           ,dpt.deptname
)DATA INITIALLY DEFERRED REFRESH IMMEDIATE;
Figure 759, Multi-table materialized query table DDL






SELECT   d.deptname
        ,d.deptno
        ,DEC(AVG(e.salary),7,2)   AS avg_sal
        ,SMALLINT(COUNT(*))       AS #emps
FROM     department d
        ,employee   e
WHERE    e.workdept    = d.deptno
  AND    d.deptname LIKE '%S%'
GROUP BY d.deptname
        ,d.deptno
HAVING   SUM(e.comm)   > 4000
ORDER BY avg_sal DESC;
Figure 760, Query that uses materialized query table






SELECT   Q2.$C0 AS "deptname"
        ,Q2.$C1 AS "deptno"
        ,Q2.$C2 AS "avg_sal"
        ,Q2.$C3 AS "#emps"
FROM    (SELECT   Q1.deptname                               AS $C0
                 ,Q1.workdept                               AS $C1
                 ,DEC((Q1.sum_salary / Q1.num_salary),7,2)  AS $C2
                 ,SMALLINT(Q1.num_rows)                     AS $C3
         FROM     dept_emp_summary AS Q1
         WHERE   (Q1.deptname LIKE '%S%')
           AND   (4000 < Q1.sum_comm)
        )AS Q2
ORDER BY Q2.$C2 DESC;
Figure 761, DB2 generated query to use materialized query table






CREATE TABLE dpt_emp_act_sumry AS
  (SELECT   emp.workdept
           ,dpt.deptname
           ,emp.empno
           ,emp.firstnme
           ,SUM(act.emptime)   AS sum_time
           ,COUNT(act.emptime) AS num_time
           ,COUNT(*)           AS num_rows
   FROM     department dpt
           ,employee   emp
           ,emp_act    act
   WHERE    dpt.deptno = emp.workdept
     AND    emp.empno  = act.empno
   GROUP BY emp.workdept
           ,dpt.deptname
           ,emp.empno
           ,emp.firstnme
)DATA INITIALLY DEFERRED REFRESH IMMEDIATE;
Figure 762, Three-table materialized query table DDL






SELECT   d.deptno
        ,d.deptname
        ,DEC(AVG(a.emptime),5,2) AS avg_time
FROM     department d
        ,employee   e
        ,emp_act    a
WHERE    d.deptno      = e.workdept
  AND    e.empno       = a.empno
  AND    d.deptname LIKE '%S%'
  AND    e.firstnme LIKE '%S%'
GROUP BY d.deptno
        ,d.deptname
ORDER BY 3 DESC;
Figure 763, Query that uses materialized query table






SELECT   Q4.$C0 AS "deptno"
        ,Q4.$C1 AS "deptname"
        ,Q4.$C2 AS "avg_time"
FROM    (SELECT   Q3.$C3                     AS $C0
                 ,Q3.$C2                     AS $C1
                 ,DEC((Q3.$C1 / Q3.$C0),5,2) AS $C2
         FROM    (SELECT   SUM(Q2.$C2)             AS $C0
                          ,SUM(Q2.$C3)             AS $C1
                          ,Q2.$C0                  AS $C2
                          ,Q2.$C1                  AS $C3
                  FROM    (SELECT   Q1.deptname          AS $C0
                                   ,Q1.workdept          AS $C1
                                   ,Q1.num_time          AS $C2
                                   ,Q1.sum_time          AS $C3
                           FROM     dpt_emp_act_sumry AS Q1
                           WHERE   (Q1.firstnme LIKE '%S%')
                             AND   (Q1.DEPTNAME LIKE '%S%')
                          )AS Q2
                  GROUP BY Q2.$C1
                          ,Q2.$C0
                 )AS Q3
        )AS Q4
ORDER BY Q4.$C2 DESC;
Figure 764, DB2 generated query to use materialized query table






CREATE INDEX dpt_emp_act_sumx1
          ON dpt_emp_act_sumry
          (workdept
          ,deptname
          ,empno
          ,firstnme);
       
CREATE INDEX dpt_emp_act_sumx2
          ON dpt_emp_act_sumry
          (num_rows);
Figure 765, Indexes for DPT_EMP_ACT_SUMRY materialized query table






SELECT   d.deptno
        ,d.deptname
        ,e.empno
        ,e.firstnme
        ,INT(AVG(a.emptime)) AS avg_time
FROM     department d
        ,employee   e
        ,emp_act    a
WHERE    d.deptno    = e.workdept
  AND    e.empno     = a.empno
  AND    d.deptno LIKE 'D%'
GROUP BY d.deptno
        ,d.deptname
        ,e.empno
        ,e.firstnme
ORDER BY 1,2,3,4;
Figure 766, Sample query that use WORKDEPT index






SELECT   d.deptno
        ,d.deptname
        ,e.empno
        ,e.firstnme
        ,COUNT(*)   AS #acts
FROM     department d
        ,employee   e
        ,emp_act    a
WHERE    d.deptno   = e.workdept
  AND    e.empno    = a.empno
GROUP BY d.deptno
        ,d.deptname
        ,e.empno
        ,e.firstnme
HAVING   COUNT(*) > 4
ORDER BY 1,2,3,4;
Figure 767, Sample query that uses NUM_ROWS index






CREATE TABLE emp_sum AS
  (SELECT   workdept
           ,job
           ,SUM(salary)        AS sum_sal
           ,COUNT(*)           AS #emps
           ,GROUPING(workdept) AS grp_dpt
           ,GROUPING(job)      AS grp_job
   FROM     employee
   GROUP BY CUBE(workdept
                ,job))
DATA INITIALLY DEFERRED REFRESH DEFERRED
ORGANIZE BY DIMENSIONS (workdept, job)
IN tsempsum;
Figure 768, Materialized query table organized by dimensions






CREATE TABLE emp_sumry AS
  (SELECT   workdept          AS dept
           ,COUNT(*)          AS #rows
           ,COUNT(salary)     AS #sal
           ,SUM(salary)       AS sum_sal
   FROM     employee emp
   GROUP BY emp.workdept
)DATA INITIALLY DEFERRED REFRESH DEFERRED;
Figure 769, Sample materialized query table






CREATE TABLE emp_sumry_s
  (dept
  ,num_rows
  ,num_sal
  ,sum_sal
  ,GLOBALTRANSID
  ,GLOBALTRANSTIME
)FOR emp_sumry PROPAGATE IMMEDIATE;
Figure 770, Staging table for the above materialized query table






SET INTEGRITY FOR emp_sumry_s STAGING IMMEDIATE UNCHECKED;
REFRESH TABLE emp_sumry;
       
<< make changes to the source table (i.e. employee) >>
       
REFRESH TABLE emp_sumry INCREMENTAL;
Figure 771, Enabling and the using a staging table






Figure 772, Identity Column syntax






CREATE TABLE invoice_data
(invoice#       INTEGER                   NOT NULL
                GENERATED ALWAYS AS IDENTITY
                   (START WITH   1
                   ,INCREMENT BY 1
                   ,NO MAXVALUE
                   ,NO CYCLE
                   ,ORDER)
,sale_date      DATE                      NOT NULL
,customer_id    CHAR(20)                  NOT NULL
,product_id     INTEGER                   NOT NULL
,quantity       INTEGER                   NOT NULL
,price          DECIMAL(18,2)             NOT NULL
,PRIMARY KEY   (invoice#));
Figure 773, Identity column, sample table






CREATE TABLE test_data                    KEY# FIELD - VALUES ASSIGNED
(key#  SMALLINT  NOT NULL                 ============================
       GENERATED ALWAYS AS IDENTITY       1 2 3 4 5 6 7 8 9 10 11 etc.
,dat1  SMALLINT  NOT NULL
,ts1   TIMESTAMP NOT NULL
,PRIMARY KEY(key#));
Figure 774, Identity column, ascending sequence






CREATE TABLE test_data                    KEY# FIELD - VALUES ASSIGNED
(key#  SMALLINT  NOT NULL                 ============================
       GENERATED ALWAYS AS IDENTITY       6 3 0 -3 -6 -9 -12 -15 etc.
                (START WITH    6
                ,INCREMENT BY -3
                ,NO CYCLE
                ,NO CACHE
                ,ORDER)
,dat1  SMALLINT  NOT NULL
,ts1   TIMESTAMP NOT NULL
,PRIMARY KEY(key#));
Figure 775, Identity column, descending sequence






CREATE TABLE test_data                    KEY# VALUES ASSIGNED
(key#  SMALLINT  NOT NULL                 ============================
       GENERATED ALWAYS AS IDENTITY       123 123 123 123 123 123 etc.
                (START WITH   123
                ,MAXVALUE     124
                ,INCREMENT BY 0
                ,NO CYCLE
                ,NO ORDER)
,dat1  SMALLINT  NOT NULL
,ts1   TIMESTAMP NOT NULL);
Figure 776, Identity column, dumb sequence






CREATE TABLE test_data                    KEY# VALUES ASSIGNED
(key#  SMALLINT  NOT NULL                 ============================
       GENERATED ALWAYS AS IDENTITY       1 3 5 2 4 6 2 4 6 2 4 6 etc.
                (START WITH    1
                ,INCREMENT BY  2
                ,MAXVALUE      6
                ,MINVALUE      2
                ,CYCLE
                ,NO CACHE
                ,ORDER)
,dat1  SMALLINT  NOT NULL
,ts1   TIMESTAMP NOT NULL);
Figure 777, Identity column, odd values, then even, then stuck






CREATE TABLE invoice_data
(invoice#       INTEGER                   NOT NULL
                GENERATED ALWAYS AS IDENTITY
                   (START WITH 100
                   ,INCREMENT BY 1
                   ,NO CYCLE
                   ,ORDER)
,sale_date      DATE                      NOT NULL
,customer_id    CHAR(20)                  NOT NULL
,product_id     INTEGER                   NOT NULL
,quantity       INTEGER                   NOT NULL
,price          DECIMAL(18,2)             NOT NULL
,PRIMARY KEY   (invoice#));
Figure 778, Identity column, definition






INSERT INTO invoice_data
VALUES (DEFAULT,'2001-11-22','ABC',123,100,10);
       
SELECT  invoice#                                                ANSWER
FROM    FINAL TABLE                                           ========
(INSERT INTO invoice_data                                     INVOICE#
(sale_date,customer_id,product_id,quantity,price)             --------
VALUES ('2002-11-22','DEF',123,100,10)                             101
      ,('2003-11-22','GHI',123,100,10));                           102
Figure 779, Invoice table, sample inserts






INVOICE#   SALE_DATE    CUSTOMER_ID   PRODUCT_ID   QUANTITY   PRICE
--------   ----------   -----------   --- ------   --------   -----
     100   2001-11-22   ABC                  123        100   10.00
     101   2002-11-22   DEF                  123        100   10.00
     102   2003-11-22   GHI                  123        100   10.00
Figure 780, Invoice table, after inserts






ALTER TABLE  invoice_data
ALTER COLUMN invoice#
   RESTART WITH 1000
   SET INCREMENT BY 2;
Figure 781, Invoice table, restart identity column value






INSERT INTO invoice_data
VALUES (DEFAULT,'2004-11-24','XXX',123,100,10)
      ,(DEFAULT,'2004-11-25','YYY',123,100,10);
Figure 782, Invoice table, more sample inserts






INVOICE#   SALE_DATE    CUSTOMER_ID   PRODUCT_ID   QUANTITY   PRICE
--------   ----------   -----------   ----------   --------   -----
     100   2001-11-22   ABC                  123        100   10.00
     101   2002-11-22   DEF                  123        100   10.00
     102   2003-11-22   GHI                  123        100   10.00
    1000   2004-11-24   XXX                  123        100   10.00
    1002   2004-11-25   YYY                  123        100   10.00
Figure 783, Invoice table, after second inserts






Figure 784, Identity Column alter syntax






CREATE TABLE customers
(cust#          INTEGER                   NOT NULL
                GENERATED ALWAYS AS IDENTITY (NO CACHE)
,cname          CHAR(10)                  NOT NULL
,ctype          CHAR(03)                  NOT NULL
,PRIMARY KEY   (cust#));
COMMIT;
       
SELECT  cust#                                                   ANSWER
FROM    FINAL TABLE                                             ======
(INSERT INTO customers                                           CUST#
 VALUES (DEFAULT,'FRED','XXX'));                                 -----
ROLLBACK;                                                            1
       
SELECT  cust#                                                   ANSWER
FROM    FINAL TABLE                                             ======
(INSERT INTO customers                                           CUST#
 VALUES (DEFAULT,'FRED','XXX'));                                 -----
COMMIT;                                                              2
Figure 785, Gaps in Values, example






SELECT  MIN(cust#) AS minc                              ANSWER
       ,MAX(cust#) AS maxc                              ==============
       ,COUNT(*)   AS rows                              MINC MAXC ROWS
FROM    FINAL TABLE                                     ---- ---- ----
(INSERT INTO customers                                     3    5    3
 VALUES (DEFAULT,'FRED','xxx')
       ,(DEFAULT,'DAVE','yyy')
       ,(DEFAULT,'JOHN','zzz'));
Figure 786, Selecting identity column values inserted






CREATE TABLE invoice_table
(invoice#       INTEGER                   NOT NULL
                GENERATED ALWAYS AS IDENTITY
,sale_date      DATE                      NOT NULL
,customer_id    CHAR(20)                  NOT NULL
,product_id     INTEGER                   NOT NULL
,quantity       INTEGER                   NOT NULL
,price          DECIMAL(18,2)             NOT NULL
,PRIMARY KEY   (invoice#));
COMMIT;
       
INSERT INTO invoice_table
VALUES (DEFAULT,'2000-11-22','ABC',123,100,10);
       
WITH temp (id) AS                                           <<< ANSWER
(VALUES (IDENTITY_VAL_LOCAL()))                                 ======
SELECT *                                                            ID
FROM   temp;                                                        --
                                                                     1
COMMIT;
       
WITH temp (id) AS                                           <<< ANSWER
(VALUES (IDENTITY_VAL_LOCAL()))                                 ======
SELECT *                                                            ID
FROM   temp;                                                        --
                                                                     1
Figure 787, IDENTITY_VAL_LOCAL function examples






INSERT INTO invoice_table
VALUES (DEFAULT,'2000-11-23','ABC',123,100,10);
       
INSERT INTO invoice_table
VALUES (DEFAULT,'2000-11-24','ABC',123,100,10)
      ,(DEFAULT,'2000-11-25','ABC',123,100,10);     ANSWER
                                                    ==================
SELECT   invoice#             AS inv#               INV# SALE_DATE  ID
        ,sale_date                                  ---- ---------- --
        ,IDENTITY_VAL_LOCAL() AS id                    1 11/22/2000  2
FROM     invoice_table                                 2 11/23/2000  2
ORDER BY 1;                                            3 11/24/2000  2
COMMIT;                                                4 11/25/2000  2
Figure 788, IDENTITY_VAL_LOCAL function examples






SELECT invoice#             AS inv#                 ANSWER
      ,sale_date                                    ==================
      ,IDENTITY_VAL_LOCAL() AS id                   INV# SALE_DATE  ID
FROM   invoice_table                                ---- ---------- --
WHERE  id = IDENTITY_VAL_LOCAL();                      2 11/23/2000  2
Figure 789, IDENTITY_VAL_LOCAL usage in predicate






CREATE SEQUENCE fred                              SEQ# VALUES ASSIGNED
   AS DECIMAL(31)                                 ====================
   START WITH 100                                 100 102 104 106 etc.
   INCREMENT BY 2
   NO MINVALUE
   NO MAXVALUE
   NO CYCLE
   CACHE 20
   ORDER;
Figure 790, Create sequence






ALTER SEQUENCE fred                               SEQ# VALUES ASSIGNED
   RESTART WITH -55                               ====================
   INCREMENT BY -5                                -55 -60 -65 -70 etc.
   MINVALUE     -1000
   MAXVALUE     +1000
   NO CACHE
   NO ORDER
   CYCLE;
Figure 791, Alter sequence attributes






CREATE SEQUENCE biggest_sale_to_date              SEQ# VALUES ASSIGNED
   AS INTEGER                                     ====================
   START WITH 345678                              345678, 345678, etc.
   INCREMENT BY 0;
Figure 792, Sequence that doesn't change






CREATE SEQUENCE fred;                                           ANSWER
COMMIT;                                                         ======
                                                                  SEQ#
WITH temp1 (n1) AS                                                ----
(VALUES 1                                                            1
 UNION ALL                                                           2
 SELECT n1 + 1                                                       3
 FROM   temp1                                                        4
 WHERE  n1 < 5                                                       5
)      
SELECT NEXTVAL FOR fred AS seq#
FROM   temp1;
Figure 793, Selecting the NEXTVAL






CREATE SEQUENCE fred;                                          ANSWERS
COMMIT;                                                        =======
       
WITH temp1 (prv) AS                                 ===>           PRV
(VALUES (PREVVAL FOR fred))                                        ---
SELECT *                                                       
FROM   temp1;
       
WITH temp1 (nxt) AS                                 ===>           NXT
(VALUES (NEXTVAL FOR fred))                                        ---
SELECT *                                                             1
FROM   temp1;
       
WITH temp1 (prv) AS                                 ===>           PRV
(VALUES (PREVVAL FOR fred))                                        ---
SELECT *                                                             1
FROM   temp1;
       
WITH temp1 (n1) AS                                  ===>       NXT PRV
(VALUES 1                                                      --- ---
 UNION ALL                                                       2   1
 SELECT n1 + 1                                                   3   1
 FROM   temp1                                                    4   1
 WHERE  n1 < 5                                                   5   1
)                                                                6   1
SELECT NEXTVAL FOR fred AS nxt
      ,PREVVAL FOR fred AS prv
FROM   temp1;
Figure 794, Use of NEXTVAL and PREVVAL expressions






CREATE SEQUENCE fred;                                          ANSWERS
COMMIT;                                                        =======
       
WITH temp1 AS                                       ===>        ID NXT
(SELECT   id                                                    -- ---
         ,NEXTVAL FOR fred AS nxt                               50   5
 FROM     staff
 WHERE    id < 100
)      
SELECT *
FROM   temp1
WHERE  id = 50 + (nxt * 0);
       
WITH temp1 (nxt, prv) AS                            ===>       NXT PRV
(VALUES (NEXTVAL FOR fred                                      --- ---
        ,PREVVAL FOR fred))                                     10   9
SELECT *
FROM   temp1;
Figure 795, NEXTVAL values used but not retrieved






CREATE SEQUENCE cust#
   START WITH   1
   INCREMENT BY 1
   NO MAXVALUE
   NO CYCLE
   ORDER;
       
CREATE TABLE us_customer
(cust#          INTEGER       NOT NULL
,cname          CHAR(10)      NOT NULL
,frst_sale      DATE          NOT NULL
,#sales         INTEGER       NOT NULL
,PRIMARY KEY   (cust#));
       
CREATE TRIGGER us_cust_ins
NO CASCADE BEFORE INSERT ON us_customer
REFERENCING NEW AS nnn
FOR EACH ROW MODE DB2SQL
SET  nnn.cust# = NEXTVAL FOR cust#;
       
CREATE TABLE intl_customer
(cust#          INTEGER       NOT NULL
,cname          CHAR(10)      NOT NULL
,frst_sale      DATE          NOT NULL
,#sales         INTEGER       NOT NULL
,PRIMARY KEY   (cust#));
       
CREATE TRIGGER intl_cust_ins
NO CASCADE BEFORE INSERT ON intl_customer
REFERENCING NEW AS nnn
FOR EACH ROW MODE DB2SQL
SET  nnn.cust# = NEXTVAL FOR cust#;
Figure 796, Create tables that use a common sequence






SELECT   cust#                                                 ANSWERS
        ,cname                                             ===========
FROM     FINAL TABLE                                       CUST# CNAME
(INSERT INTO us_customer (cname, frst_sale, #sales)        ----- -----
 VALUES ('FRED','2002-10-22',1)                                1 FRED
       ,('JOHN','2002-10-23',1));                              2 JOHN
       
SELECT   cust#
        ,cname
FROM     FINAL TABLE                                       CUST# CNAME
(INSERT INTO intl_customer (cname, frst_sale, #sales)      ----- -----
 VALUES ('SUE','2002-11-12',2)                                 3 SUE
       ,('DEB','2002-11-13',2));                               4 DEB
Figure 797, Insert into tables with common sequence






WITH temp (prev) AS                                             ANSWER
(VALUES (PREVVAL FOR cust#))                                    ======
SELECT *                                                          PREV
FROM   temp;                                                      ----
                                                                     4
Figure 798, Get previous value - select






VALUES PREVVAL FOR CUST# INTO :host-var
Figure 799, Get previous value - into host-variable






CREATE SEQUENCE delete_rows
   START WITH   1
   INCREMENT BY 1
   NO MAXVALUE
   NO CYCLE
   ORDER;
       
CREATE SEQUENCE delete_stmts
   START WITH   1
   INCREMENT BY 1
   NO MAXVALUE
   NO CYCLE
   ORDER;
       
CREATE TABLE customer
(cust#          INTEGER       NOT NULL
,cname          CHAR(10)      NOT NULL
,frst_sale      DATE          NOT NULL
,#sales         INTEGER       NOT NULL
,PRIMARY KEY   (cust#));
       
CREATE TRIGGER cust_del_rows
AFTER DELETE ON customer
FOR EACH ROW MODE DB2SQL
  WITH temp1 (n1) AS (VALUES(1))
  SELECT NEXTVAL FOR delete_rows
  FROM   temp1;
       
CREATE TRIGGER cust_del_stmts
AFTER DELETE ON customer
FOR EACH STATEMENT MODE DB2SQL
  WITH temp1 (n1) AS (VALUES(1))
  SELECT NEXTVAL FOR delete_stmts
  FROM   temp1;
Figure 800, Count deletes done to table






CREATE TABLE sales_invoice
(invoice#       INTEGER                   NOT NULL
,sale_date      DATE                      NOT NULL
,customer_id    CHAR(20)                  NOT NULL
,product_id     INTEGER                   NOT NULL
,quantity       INTEGER                   NOT NULL
,price          DECIMAL(18,2)             NOT NULL
,PRIMARY KEY   (invoice#));
Figure 801, Sample table, roll your own sequence#






CREATE TRIGGER sales_insert
NO CASCADE BEFORE
INSERT ON sales_invoice
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
  SET nnn.invoice# =
     (SELECT COALESCE(MAX(invoice#),0) + 1
      FROM   sales_invoice);
Figure 802, Sample trigger, roll your own sequence#






INSERT INTO sales_invoice VALUES (0,'2001-06-22','ABC',123,10,1);
INSERT INTO sales_invoice VALUES (0,'2001-06-23','DEF',453,10,1);
COMMIT;
       
INSERT INTO sales_invoice VALUES (0,'2001-06-24','XXX',888,10,1);
ROLLBACK;
       
INSERT INTO sales_invoice VALUES (0,'2001-06-25','YYY',999,10,1);
COMMIT;
                                    ANSWER
        ==============================================================
        INVOICE#  SALE_DATE   CUSTOMER_ID  PRODUCT_ID  QUANTITY  PRICE
        --------  ----------  -----------  ----------  --------  -----
               1  06/22/2001  ABC                 123        10   1.00
               2  06/23/2001  DEF                 453        10   1.00
               3  06/25/2001  YYY                 999        10   1.00
Figure 803, Sample inserts, roll your own sequence#






CREATE TABLE control_table
(table_name     CHAR(18)    NOT NULL
,table_nmbr     INTEGER     NOT NULL
,PRIMARY KEY (table_name));
Figure 804, Control Table, DDL






INSERT INTO control_table VALUES ('invoice_table',0);
INSERT INTO control_table VALUES ('2nd_data_tble',0);
INSERT INTO control_table VALUES ('3rd_data_tble',0);
Figure 805, Control Table, sample inserts






CREATE TABLE invoice_table
(unqval         CHAR(13) FOR BIT DATA     NOT NULL
,invoice#       INTEGER
,sale_date      DATE                      NOT NULL
,customer_id    CHAR(20)                  NOT NULL
,product_id     INTEGER                   NOT NULL
,quantity       INTEGER                   NOT NULL
,price          DECIMAL(18,2)             NOT NULL
,PRIMARY KEY(unqval));
Figure 806, Sample Data Table, DDL






CREATE TRIGGER invoice1
NO CASCADE BEFORE INSERT ON invoice_table
REFERENCING NEW AS nnn
FOR EACH ROW MODE DB2SQL
   SET nnn.unqval   = GENERATE_UNIQUE()
      ,nnn.invoice# = NULL;
Figure 807, Before trigger






CREATE TRIGGER invoice2
AFTER INSERT ON invoice_table
REFERENCING NEW AS nnn
FOR EACH ROW MODE DB2SQL
BEGIN ATOMIC
   UPDATE control_table
   SET    table_nmbr =  table_nmbr + 1
   WHERE  table_name = 'invoice_table';
   UPDATE invoice_table
   SET    invoice# =
         (SELECT table_nmbr
          FROM   control_table
          WHERE  table_name = 'invoice_table')
   WHERE  unqval    = nnn.unqval
     AND  invoice# IS NULL;
END    
Figure 808, After trigger






CREATE TRIGGER invoice3
NO CASCADE BEFORE UPDATE OF invoice# ON invoice_table
REFERENCING OLD AS ooo
            NEW AS nnn
FOR EACH ROW MODE DB2SQL
WHEN (ooo.invoice# <> nnn.invoice#)
   SIGNAL SQLSTATE '71001' ('no updates allowed - you twit');
Figure 809, Update trigger






SELECT   id
        ,salary
FROM    (SELECT   s.*
                 ,ROW_NUMBER() OVER(ORDER BY salary DESC) AS sorder
         FROM     staff s
         WHERE    id < 200                               ANSWER
        )AS xxx                                          =============
WHERE    sorder BETWEEN 2 AND 3                          ID   SALARY
ORDER BY id;                                             ---  --------
                                                          50  20659.80
                                                         140  21150.00
Figure 810, Nested Table Expression






WITH xxx (id, salary, sorder) AS
(SELECT  ID
        ,salary
        ,ROW_NUMBER() OVER(ORDER BY salary DESC) AS sorder
 FROM    staff
 WHERE   id < 200
)                                                        ANSWER
SELECT   id                                              =============
        ,salary                                          ID   SALARY
FROM     xxx                                             ---  --------
WHERE    sorder BETWEEN 2 AND 3                           50  20659.80
ORDER BY id;                                             140  21150.00
Figure 811, Common Table Expression






WITH                                  ANSWER
rows_wanted AS                        ================================
   (SELECT  *                         ID NAME    SALARY   SUM_SAL  PCT
    FROM    staff                     -- ------- -------- -------- ---
    WHERE   id             < 100      70 Rothman 16502.83 34504.58  47
      AND   UCASE(name) LIKE '%T%'    90 Koonitz 18001.75 34504.58  52
    ), 
sum_salary  AS
   (SELECT  SUM(salary) AS sum_sal
    FROM    rows_wanted)
SELECT   id
        ,name
        ,salary
        ,sum_sal
        ,INT((salary * 100) / sum_sal) AS pct
FROM     rows_wanted
        ,sum_salary
ORDER BY id;
Figure 812, Common Table Expression






DECLARE GLOBAL TEMPORARY TABLE session.fred
(dept         SMALLINT    NOT NULL
,avg_salary   DEC(7,2)    NOT NULL
,num_emps     SMALLINT    NOT NULL)
ON COMMIT PRESERVE ROWS;
COMMIT;
       
INSERT INTO session.fred
SELECT   dept
        ,AVG(salary)
        ,COUNT(*)                           ANSWER#1
FROM     staff                              ========
WHERE    id > 200                           CNT
GROUP BY dept;                              ---
COMMIT;                                       4
       
SELECT  COUNT(*) AS cnt
FROM    session.fred;                       ANSWER#2
                                            ==========================
DELETE FROM session.fred                    DEPT  AVG_SALARY  NUM_EMPS
WHERE  dept > 80;                           ----  ----------  --------
                                              10    20168.08         3
SELECT  *                                     51    15161.43         3
FROM    session.fred;                         66    17215.24         5
Figure 813, Declared Global Temporary Table






WITH staff_dept AS                          ANSWER
(SELECT   dept        AS dept#              ==========================
         ,MAX(salary) AS max_sal            ID  DEPT  SALARY   MAX_SAL
 FROM     staff                             --- ---- -------- --------
 WHERE    dept < 50                          10   20 18357.50 18357.50
 GROUP BY dept                              190   20 14252.75 18357.50
)                                           200   42 11508.60 18352.80
SELECT   id                                 220   51 17654.50        -
        ,dept
        ,salary
        ,max_sal
FROM     staff
LEFT OUTER JOIN
         staff_dept
ON       dept    = dept#
WHERE    name LIKE 'S%'
ORDER BY id;
Figure 814, Identical query (1 of 3) - using Common Table Expression






SELECT   id                                 ANSWER
        ,dept                               ==========================
        ,salary                             ID  DEPT  SALARY   MAX_SAL
        ,max_sal                            --- ---- -------- --------
FROM     staff                               10   20 18357.50 18357.50
LEFT OUTER JOIN                             190   20 14252.75 18357.50
        (SELECT   dept        AS dept#      200   42 11508.60 18352.80
                 ,MAX(salary) AS max_sal    220   51 17654.50        -
         FROM     staff
         WHERE    dept < 50
         GROUP BY dept
        )AS STAFF_dept
ON       dept    = dept#
WHERE    name LIKE 'S%'
ORDER BY id;
Figure 815, Identical query (2 of 3) - using fullselect in FROM






SELECT   id                                 ANSWER
        ,dept                               ==========================
        ,salary                             ID  DEPT  SALARY   MAX_SAL
        ,(SELECT   MAX(salary)              --- ---- -------- --------
          FROM     staff s2                  10   20 18357.50 18357.50
          WHERE    s1.dept = s2.dept        190   20 14252.75 18357.50
            AND    s2.dept < 50             200   42 11508.60 18352.80
          GROUP BY dept)                    220   51 17654.50        -
         AS max_sal
FROM     staff s1
WHERE    name LIKE 'S%'
ORDER BY id;
Figure 816, Identical query (3 of 3) - using fullselect in SELECT






Figure 817, Common Table Expression Syntax






WITH temp1 AS                                       ANSWER
(SELECT MAX(name) AS max_name                       ==================
       ,MAX(dept) AS max_dept                       MAX_NAME  MAX_DEPT
 FROM   staff                                       --------- --------
)                                                   Yamaguchi       84
SELECT *
FROM   temp1;
Figure 818, Common Table Expression, using named fields






WITH temp1 (max_name,max_dept) AS                   ANSWER
(SELECT MAX(name)                                   ==================
       ,MAX(dept)                                   MAX_NAME  MAX_DEPT
 FROM   staff                                       --------- --------
)                                                   Yamaguchi       84
SELECT *
FROM   temp1;
Figure 819, Common Table Expression, using unnamed fields






WITH                                                        ANSWER
temp1 AS                                                    ==========
  (SELECT   dept                                            MAX_AVG
           ,AVG(salary)  AS avg_sal                         ----------
   FROM     staff                                           20865.8625
   GROUP BY dept),
temp2 AS
  (SELECT   MAX(avg_sal) AS max_avg
   FROM     temp1)
SELECT *
FROM   temp2;
Figure 820, Query with two common table expressions






SELECT *                                                    ANSWER
FROM  (SELECT MAX(avg_sal) AS max_avg                       ==========
       FROM  (SELECT dept                                   MAX_AVG
                    ,AVG(salary) AS avg_sal                 ----------
              FROM   staff                                  20865.8625
              GROUP BY dept
             )AS temp1
      )AS temp2;
Figure 821, Same as prior example, but using nested table expressions






WITH temp1 AS                               ANSWER
(SELECT   id                                ==========================
         ,name                              ID  DEPT  SALARY   MAX_SAL
         ,dept                              --- ---- -------- --------
         ,salary                             10   20 18357.50 18357.50
 FROM     staff                             190   20 14252.75 18357.50
 WHERE    id      <  300                    200   42 11508.60 11508.60
   AND    dept   <>  55                     220   51 17654.50 17654.50
   AND    name LIKE 'S%'
   AND    dept NOT IN
         (SELECT deptnumb
          FROM   org
          WHERE  division = 'SOUTHERN'
             OR  location = 'HARTFORD')
)      
,temp2 AS
(SELECT   dept
         ,MAX(salary) AS max_sal
 FROM     temp1
 GROUP BY dept
)      
SELECT   t1.id
        ,t1.dept
        ,t1.salary
        ,t2.max_sal
FROM     temp1 t1
        ,temp2 t2
WHERE    t1.dept = t2.dept
ORDER BY t1.id;
Figure 822, Deriving second temporary table from first






INSERT INTO staff
WITH temp1 (max1) AS
(SELECT MAX(id) + 1
 FROM   staff
)      
SELECT max1,'A',1,'B',2,3,4
FROM   temp1;
Figure 823, Insert using common table expression






INSERT INTO staff
SELECT MAX(id) + 1
      ,'A',1,'B',2,3,4
FROM   staff;
Figure 824, Equivalent insert (to above) without common table expression






SELECT   division
        ,DEC(AVG(dept_avg),7,2) AS div_dept
        ,COUNT(*)               AS #dpts
        ,SUM(#emps)             AS #emps
FROM    (SELECT   division
                 ,dept
                 ,AVG(salary) AS dept_avg
                 ,COUNT(*)    AS #emps
         FROM     staff                            ANSWER
                 ,org                   ==============================
         WHERE    dept = deptnumb       DIVISION  DIV_DEPT #DPTS #EMPS
         GROUP BY division              --------- -------- ----- -----
                 ,dept                  Corporate 20865.86     1     4
        )AS xxx                         Eastern   15670.32     3    13
GROUP BY division;                      Midwest   15905.21     2     9
                                        Western   16875.99     2     9
Figure 825, Nested column function usage






SELECT id                                                       ANSWER
FROM  (SELECT *                                                 ======
       FROM  (SELECT id, years, salary                             ID
              FROM  (SELECT *                                      ---
                     FROM   (SELECT *                              170
                             FROM   staff                          180
                             WHERE  dept < 77                      230
                            )AS t1
                     WHERE  id  < 300
                    )AS t2
              WHERE  job LIKE 'C%'
             )AS t3
       WHERE  salary < 18000
      )AS t4
WHERE  years < 5;
Figure 826, Nested fullselects






SELECT   a.id                                ANSWER
        ,a.dept                              =========================
        ,a.salary                            ID DEPT  SALARY  AVG_DEPT
        ,DEC(b.avgsal,7,2) AS avg_dept       -- ---- -------- --------
FROM     staff  a                            10   20 18357.50 16071.52
LEFT OUTER JOIN                              20   20 78171.25 16071.52
        (SELECT   dept        AS dept        30   38 77506.75        -
                 ,AVG(salary) AS avgsal
         FROM     staff
         GROUP BY dept
         HAVING   AVG(salary) > 16000
        )AS b
ON       a.dept = b.dept
WHERE    a.id   < 40
ORDER BY a.id;
Figure 827, Join fullselect to real table






SELECT   a.id                                ANSWER
        ,a.dept                              =========================
        ,a.salary                            ID DEPT SALARY   DEPTSAL
        ,b.deptsal                           -- ---- -------- --------
FROM     staff  a                            10 20   18357.50 64286.10
        ,TABLE                               20 20   78171.25 64286.10
        (SELECT   b.dept                     30 38   77506.75 77285.55
                 ,SUM(b.salary) AS deptsal
         FROM     staff b
         WHERE    b.dept = a.dept
         GROUP BY b.dept
        )AS b
WHERE    a.id   < 40
ORDER BY a.id;
Figure 828, Fullselect with external table reference






SELECT   a.id                                ANSWER
        ,a.dept                              =========================
        ,a.salary                            ID DEPT SALARY   DEPTSAL
        ,b.deptsal                           -- ---- -------- --------
FROM     staff  a                            10 20   18357.50 64286.10
       ,(SELECT   b.dept                     20 20   78171.25 64286.10
                 ,SUM(b.salary) AS deptsal   30 38   77506.75 77285.55
         FROM     staff b
         GROUP BY b.dept
        )AS b
WHERE    a.id   < 40
  AND    b.dept = a.dept
ORDER BY a.id;
Figure 829, Fullselect without external table reference






SELECT   id                                       ANSWER
        ,salary                                   ====================
        ,(SELECT MAX(salary)                      ID SALARY   MAXSAL
          FROM   staff                            -- -------- --------
         ) AS maxsal                              10 18357.50 22959.20
FROM     staff  a                                 20 78171.25 22959.20
WHERE    id  < 60                                 30 77506.75 22959.20
ORDER BY id;                                      40 18006.00 22959.20
                                                  50 20659.80 22959.20
Figure 830, Use an uncorrelated Full-Select in a SELECT list






SELECT   id                                       ANSWER
        ,salary                                   ====================
        ,(SELECT MAX(salary)                      ID SALARY   MAXSAL
          FROM   staff  b                         -- -------- --------
          WHERE  a.dept = b.dept                  10 18357.50 18357.50
         ) AS maxsal                              20 78171.25 18357.50
FROM     staff  a                                 30 77506.75 18006.00
WHERE    id  < 60                                 40 18006.00 18006.00
ORDER BY id;                                      50 20659.80 20659.80
Figure 831, Use a correlated Full-Select in a SELECT list






SELECT id                           ANSWER
      ,dept                         ==================================
      ,salary                       ID DEPT  SALARY  4        5
      ,(SELECT MAX(salary)          -- ---- -------- -------- --------
        FROM   staff b              10   20 18357.50 18357.50 22959.20
        WHERE  b.dept = a.dept)     20   20 78171.25 18357.50 22959.20
      ,(SELECT MAX(salary)          30   38 77506.75 18006.00 22959.20
        FROM   staff)               40   38 18006.00 18006.00 22959.20
FROM   staff a                      50   15 20659.80 20659.80 22959.20
WHERE  id  < 60
ORDER BY id;
Figure 832, Use correlated and uncorrelated Full-Selects in a SELECT list






INSERT INTO staff
SELECT  id + 1
       ,(SELECT MIN(name)
         FROM   staff)
       ,(SELECT dept
         FROM   staff s2
         WHERE  s2.id = s1.id - 100)
       ,'A',1,2,3
FROM    staff s1
WHERE   id =
       (SELECT MAX(id)
        FROM   staff);
Figure 833, Fullselect in INSERT






UPDATE staff a                               ANSWER:     SALARY
SET    salary =                              ======= =================
       (SELECT AVG(salary)+ 2000             ID DEPT BEFORE   AFTER
        FROM   staff)                        -- ---- -------- --------
WHERE  id  < 60;                             10   20 18357.50 18675.64
                                             20   20 78171.25 18675.64
                                             30   38 77506.75 18675.64
                                             40   38 18006.00 18675.64
                                             50   15 20659.80 18675.64
Figure 834, Use uncorrelated Full-Select to give workers company AVG salary (+$2000)






UPDATE staff a                               ANSWER:     SALARY
SET    salary =                              ======= =================
       (SELECT AVG(salary) + 2000            ID DEPT BEFORE   AFTER
        FROM   staff  b                      -- ---- -------- --------
        WHERE  a.dept = b.dept )             10   20 18357.50 18071.52
WHERE  id  < 60;                             20   20 78171.25 18071.52
                                             30   38 77506.75 17457.11
                                             40   38 18006.00 17457.11
                                             50   15 20659.80 17482.33
Figure 835, Use correlated Full-Select to give workers department AVG salary (+$2000)






UPDATE staff a
SET    (salary,years) =
       (SELECT AVG(salary) + 2000
              ,MAX(years)
        FROM   staff  b
        WHERE  a.dept = b.dept )
WHERE  id  < 60;
Figure 836, Update two fields by referencing Full-Select






Figure 837, Declared Global Temporary Table syntax






DECLARE GLOBAL TEMPORARY TABLE session.fred
(dept         SMALLINT    NOT NULL
,avg_salary   DEC(7,2)    NOT NULL
,num_emps     SMALLINT    NOT NULL)
ON COMMIT DELETE ROWS;
Figure 838, Declare Global Temporary Table - define columns






DECLARE GLOBAL TEMPORARY TABLE session.fred
LIKE staff INCLUDING COLUMN DEFAULTS
WITH REPLACE
ON COMMIT PRESERVE ROWS;
Figure 839, Declare Global Temporary Table - like another table






DECLARE GLOBAL TEMPORARY TABLE session.fred AS
(SELECT   dept
         ,MAX(id)     AS max_id
         ,SUM(salary) AS sum_sal
 FROM     staff
 WHERE    name <> 'IDIOT'
 GROUP BY dept)
DEFINITION ONLY
WITH REPLACE;
Figure 840, Declare Global Temporary Table - like query output






DECLARE GLOBAL TEMPORARY TABLE session.fred
LIKE staff INCLUDING COLUMN DEFAULTS
WITH REPLACE ON COMMIT DELETE ROWS;
       
CREATE UNIQUE INDEX session.fredx ON Session.fred (id);
       
INSERT INTO session.fred
SELECT   *
FROM     staff
WHERE    id < 200;
                                                                ANSWER
SELECT  COUNT(*)                                                ======
FROM    session.fred;                                               19
       
COMMIT;
                                                                ANSWER
SELECT  COUNT(*)                                                ======
FROM    session.fred;                                                0
Figure 841, Temporary table with index






DECLARE GLOBAL TEMPORARY TABLE session.fred
(dept         SMALLINT    NOT NULL
,avg_salary   DEC(7,2)    NOT NULL
,num_emps     SMALLINT    NOT NULL)
ON COMMIT DELETE ROWS;
       
INSERT INTO session.fred
SELECT   dept
        ,AVG(salary)
        ,COUNT(*)
FROM     staff
GROUP BY dept;
                                                                ANSWER
SELECT  COUNT(*)                                                ======
FROM    session.fred;                                                8
       
DROP TABLE session.fred;
       
DECLARE GLOBAL TEMPORARY TABLE session.fred
(dept         SMALLINT    NOT NULL)
ON COMMIT DELETE ROWS;
                                                                ANSWER
SELECT  COUNT(*)                                                ======
FROM    session.fred;                                                0
Figure 842, Dropping a temporary table






CREATE USER TEMPORARY TABLESPACE FRED
MANAGED BY DATABASE
USING (FILE 'C:\DB2\TEMPFRED\FRED1' 1000
      ,FILE 'C:\DB2\TEMPFRED\FRED2' 1000
      ,FILE 'C:\DB2\TEMPFRED\FRED3' 1000);
       
GRANT USE OF TABLESPACE FRED TO PUBLIC;
Figure 843, Create USER TEMPORARY tablespace






HIERARCHY                                                 AAA
+---------------+                                          |
|PKEY |CKEY |NUM|                                    +-----+-----+
|-----|-----|---|                                    |     |     |
|AAA  |BBB  |  1|                                   BBB   CCC   DDD
|AAA  |CCC  |  5|                                          |     |
|AAA  |DDD  | 20|                                          +-+ +-+--+
|CCC  |EEE  | 33|                                            | |    |
|DDD  |EEE  | 44|                                            EEE   FFF
|DDD  |FFF  |  5|                                                   |
|FFF  |GGG  |  5|                                                   |
+---------------+                                                  GGG
Figure 844, Sample Table description - Recursion






WITH parent (pkey, ckey) AS                   ANSWER
  (SELECT pkey, ckey                         =========      PROCESSING
   FROM   hierarchy                          PKEY CKEY       SEQUENCE
   WHERE  pkey = 'AAA'                       ---- ----      ==========
   UNION ALL                                 AAA  BBB     < 1st pass
   SELECT C.pkey, C.ckey                     AAA  CCC       ""
   FROM   hierarchy C                        AAA  DDD       ""
         ,parent    P                        CCC  EEE     < 2nd pass
   WHERE  P.ckey = C.pkey                    DDD  EEE     < 3rd pass
  )                                          DDD  FFF       ""
SELECT pkey, ckey                            FFF  GGG     < 4th pass
FROM   parent;
Figure 845, SQL that does Recursion






Figure 846, Recursive processing sequence






CREATE TABLE hierarchy
(pkey     CHAR(03)     NOT NULL
,ckey     CHAR(03)     NOT NULL
,num      SMALLINT     NOT NULL
,PRIMARY KEY(pkey, ckey)
,CONSTRAINT dt1 CHECK (pkey <> ckey)
,CONSTRAINT dt2 CHECK (num   > 0));
COMMIT;
       
CREATE UNIQUE INDEX hier_x1 ON hierarchy
(ckey, pkey);
COMMIT;
       
INSERT INTO hierarchy VALUES
('AAA','BBB', 1),
('AAA','CCC', 5),
('AAA','DDD',20),
('CCC','EEE',33),
('DDD','EEE',44),
('DDD','FFF', 5),
('FFF','GGG', 5);
COMMIT;
Figure 847, Sample Table DDL - Recursion






WITH parent (ckey) AS                       ANSWER   HIERARCHY
  (SELECT ckey                              ======   +---------------+
   FROM   hierarchy                         CKEY     |PKEY |CKEY |NUM|
   WHERE  pkey = 'AAA'                      ----     |-----|-----|---|
   UNION ALL                                BBB      |AAA  |BBB  |  1|
   SELECT C.ckey                            CCC      |AAA  |CCC  |  5|
   FROM   hierarchy C                       DDD      |AAA  |DDD  | 20|
         ,parent    P                       EEE      |CCC  |EEE  | 33|
   WHERE  P.ckey = C.pkey                   EEE      |DDD  |EEE  | 44|
  )                                         FFF      |DDD  |FFF  |  5|
SELECT ckey                                 GGG      |FFF  |GGG  |  5|
FROM   parent;                                       +---------------+
Figure 848, List of children of AAA






WITH parent (ckey) AS                       ANSWER   HIERARCHY
  (SELECT DISTINCT pkey                     ======   +---------------+
   FROM   hierarchy                         CKEY     |PKEY |CKEY |NUM|
   WHERE  pkey = 'AAA'                      ----     |-----|-----|---|
   UNION ALL                                AAA      |AAA  |BBB  |  1|
   SELECT C.ckey                            BBB      |AAA  |CCC  |  5|
   FROM   hierarchy C                       CCC      |AAA  |DDD  | 20|
         ,parent    P                       DDD      |CCC  |EEE  | 33|
   WHERE  P.ckey = C.pkey                   EEE      |DDD  |EEE  | 44|
  )                                         EEE      |DDD  |FFF  |  5|
SELECT ckey                                 FFF      |FFF  |GGG  |  5|
FROM   parent;                              GGG      +---------------+
Figure 849, List all children of AAA






WITH parent (ckey) AS                       ANSWER   HIERARCHY
  (SELECT DISTINCT pkey                     ======   +---------------+
   FROM   hierarchy                         CKEY     |PKEY |CKEY |NUM|
   WHERE  pkey = 'AAA'                      ----     |-----|-----|---|
   UNION ALL                                AAA      |AAA  |BBB  |  1|
   SELECT C.ckey                            BBB      |AAA  |CCC  |  5|
   FROM   hierarchy C                       CCC      |AAA  |DDD  | 20|
         ,parent    P                       DDD      |CCC  |EEE  | 33|
   WHERE  P.ckey = C.pkey                   EEE      |DDD  |EEE  | 44|
  )                                         FFF      |DDD  |FFF  |  5|
SELECT DISTINCT ckey                        GGG      |FFF  |GGG  |  5|
FROM   parent;                                       +---------------+
Figure 850, List distinct children of AAA






WITH parent (ckey) AS                       ANSWER   HIERARCHY
  (SELECT DISTINCT pkey                     ======   +---------------+
   FROM   hierarchy                         CKEY     |PKEY |CKEY |NUM|
   WHERE  pkey = 'AAA'                      ----     |-----|-----|---|
   UNION ALL                                AAA      |AAA  |BBB  |  1|
   SELECT C.ckey                            BBB      |AAA  |CCC  |  5|
   FROM   hierarchy C                       CCC      |AAA  |DDD  | 20|
         ,parent    P                       DDD      |CCC  |EEE  | 33|
   WHERE  P.ckey = C.pkey                   EEE      |DDD  |EEE  | 44|
  ),                                        FFF      |DDD  |FFF  |  5|
distinct_parent (ckey) AS                   GGG      |FFF  |GGG  |  5|
  (SELECT DISTINCT ckey                              +---------------+
   FROM   parent
  )    
SELECT ckey
FROM   distinct_parent;
Figure 851, List distinct children of AAA






WITH parent (ckey, lvl) AS               ANSWER           AAA
  (SELECT DISTINCT pkey, 0               ========          |
   FROM   hierarchy                      CKEY LVL    +-----+-----+
   WHERE  pkey = 'AAA'                   ---- ---    |     |     |
   UNION ALL                             AAA    0   BBB   CCC   DDD
   SELECT C.ckey, P.lvl +1               BBB    1          |     |
   FROM   hierarchy C                    CCC    1          +-+ +-+--+
         ,parent    P                    DDD    1            | |    |
   WHERE  P.ckey = C.pkey                EEE    2            EEE   FFF
  )                                      EEE    2                   |
SELECT ckey, lvl                         FFF    2                   |
FROM   parent;                           GGG    3                  GGG
Figure 852, Show item level in hierarchy






WITH parent (ckey, lvl) AS                ANSWER     HIERARCHY
  (SELECT DISTINCT pkey, 0                ========   +---------------+
   FROM   hierarchy                       CKEY LVL   |PKEY |CKEY |NUM|
   WHERE  pkey = 'AAA'                    ---- ---   |-----|-----|---|
   UNION ALL                              AAA    0   |AAA  |BBB  |  1|
   SELECT C.ckey, P.lvl +1                BBB    1   |AAA  |CCC  |  5|
   FROM   hierarchy C                     CCC    1   |AAA  |DDD  | 20|
         ,parent    P                     DDD    1   |CCC  |EEE  | 33|
   WHERE  P.ckey  = C.pkey                EEE    2   |DDD  |EEE  | 44|
  )                                       EEE    2   |DDD  |FFF  |  5|
SELECT ckey, lvl                          FFF    2   |FFF  |GGG  |  5|
FROM   parent                                        +---------------+
WHERE  lvl < 3;
Figure 853, Select rows where LEVEL < 3






WITH parent (ckey, lvl) AS               ANSWER           AAA
  (SELECT DISTINCT pkey, 0               ========          |
   FROM   hierarchy                      CKEY LVL    +-----+-----+
   WHERE  pkey = 'AAA'                   ---- ---    |     |     |
   UNION ALL                             AAA    0   BBB   CCC   DDD
   SELECT C.ckey, P.lvl +1               BBB    1          |     |
   FROM   hierarchy C                    CCC    1          +-+ +-+--+
         ,parent    P                    DDD    1            | |    |
   WHERE  P.ckey  = C.pkey               EEE    2            EEE   FFF
     AND  P.lvl+1 < 3                    EEE    2                   |
  )                                      FFF    2                   |
SELECT ckey, lvl                                                   GGG
FROM   parent;
Figure 854, Select rows where LEVEL < 3






WITH parent (ckey, lvl) AS                ANSWER     HIERARCHY
  (SELECT DISTINCT pkey, 0                ========   +---------------+
   FROM   hierarchy                       CKEY LVL   |PKEY |CKEY |NUM|
   WHERE  pkey = 'AAA'                    ---- ---   |-----|-----|---|
   UNION ALL                              EEE    2   |AAA  |BBB  |  1|
   SELECT C.ckey, P.lvl +1                EEE    2   |AAA  |CCC  |  5|
   FROM   hierarchy C                     FFF    2   |AAA  |DDD  | 20|
         ,parent    P                                |CCC  |EEE  | 33|
   WHERE  P.ckey  = C.pkey                           |DDD  |EEE  | 44|
     AND  P.lvl+1 < 3                                |DDD  |FFF  |  5|
  )                                                  |FFF  |GGG  |  5|
SELECT ckey, lvl                                     +---------------+
FROM   parent
WHERE  lvl = 2;
Figure 855, Select rows where LEVEL = 2






WITH children (kkey, lvl) AS             ANSWER           AAA
  (SELECT ckey, 1                        ========          |
   FROM   hierarchy                      KKEY LVL    +-----+-----+
   WHERE  pkey = 'DDD'                   ---- ---    |     |     |
   UNION ALL                             AAA   -1   BBB   CCC   DDD
   SELECT H.ckey, C.lvl + 1              EEE    1          |     |
   FROM   hierarchy H                    FFF    1          +-+ +-+--+
         ,children  C                    GGG    2            | |    |
   WHERE  H.pkey = C.kkey                                    EEE   FFF
  )                                                                 |
,parents (kkey, lvl) AS                                             |
  (SELECT pkey, -1                                                 GGG
   FROM   hierarchy
   WHERE  ckey = 'DDD'
   UNION ALL
   SELECT H.pkey, P.lvl - 1
   FROM   hierarchy H
         ,parents   P
   WHERE  H.ckey = P.kkey
  )    
SELECT   kkey ,lvl
FROM     children
UNION ALL
SELECT   kkey ,lvl
FROM     parents;
Figure 856, Find all children and parents of DDD






WITH temp1 (n1) AS                                              ANSWER
  (SELECT id                                                    ======
   FROM   staff                                                     N1
   WHERE  id = 10                                                   --
   UNION ALL                                                      warn
   SELECT n1 +10                                                    10
   FROM   temp1                                                     20
   WHERE  n1 <  50                                                  30
  )                                                                 40
SELECT *                                                            50
FROM   temp1;
Figure 857, Recursion - with warning message






WITH temp1 (n1) AS                                              ANSWER
  (SELECT INT(id)                                               ======
   FROM   staff                                                     N1
   WHERE  id = 10                                                   --
   UNION ALL                                                        10
   SELECT n1 +10                                                    20
   FROM   temp1                                                     30
   WHERE  n1 <  50                                                  40
  )                                                                 50
SELECT *
FROM   temp1;
Figure 858, Recursion - without warning message






DIVERGENT      CONVERGENT      RECURSIVE       BALANCED     UNBALANCED
=========      ==========      =========       ========     ==========
       
  AAA            AAA             AAA<--+         AAA           AAA
   |              |               |    |          |             |
 +-+-+          +-+-+           +-+-+  |        +-+-+         +-+-+
 |   |          |   |           |   |  |        |   |         |   |
BBB CCC        BBB CCC         BBB CCC>+       BBB CCC       BBB CCC
     |          |   |               |           |   |             |
   +-+-+        +-+-+-+           +-+-+         |   +---+       +-+-+
   |   |          |   |           |   |         |   |   |       |   |
  DDD EEE        DDD EEE         DDD EEE       DDD EEE FFF     DDD EEE
Figure 859, Hierarchy Flavours






OBJECTS_RELATES                                           AAA
+---------------------+                                    |
|KEYO |PKEY |NUM|PRICE|                              +-----+-----+
|-----|-----|---|-----|                              |     |     |
|AAA  |     |   |  $10|                             BBB   CCC   DDD
|BBB  |AAA  |  1|  $21|                                          |
|CCC  |AAA  |  5|  $23|                                       +--+--+
|DDD  |AAA  | 20|  $25|                                       |     |
|EEE  |DDD  | 44|  $33|                                      EEE   FFF
|FFF  |DDD  |  5|  $34|                                             |
|GGG  |FFF  |  5|  $44|                                             |
+---------------------+                                            GGG
Figure 860, Divergent Hierarchy - Table and Layout






OBJECTS          RELATIONSHIPS                            AAA
+-----------+    +---------------+                         |
|KEYO |PRICE|    |PKEY |CKEY |NUM|                   +-----+-----+
|-----|-----|    |-----|-----|---|                   |     |     |
|AAA  |  $10|    |AAA  |BBB  |  1|                  BBB   CCC   DDD
|BBB  |  $21|    |AAA  |CCC  |  5|                         |     |
|CCC  |  $23|    |AAA  |DDD  | 20|                         +-+ +-+--+
|DDD  |  $25|    |CCC  |EEE  | 33|                           | |    |
|EEE  |  $33|    |DDD  |EEE  | 44|                           EEE   FFF
|FFF  |  $34|    |DDD  |FFF  |  5|                                  |
|GGG  |  $44|    |FFF  |GGG  |  5|                                  |
+-----------+    +---------------+                                 GGG
Figure 861, Convergent Hierarchy - Tables and Layout






OBJECTS          RELATIONSHIPS                            AAA <------+
+-----------+    +---------------+                         |         |
|KEYO |PRICE|    |PKEY |CKEY |NUM|                   +-----+-----+   |
|-----|-----|    |-----|-----|---|                   |     |     |   |
|AAA  |  $10|    |AAA  |BBB  |  1|                  BBB   CCC   DDD>-+
|BBB  |  $21|    |AAA  |CCC  |  5|                         |     |
|CCC  |  $23|    |AAA  |DDD  | 20|                         +-+ +-+--+
|DDD  |  $25|    |CCC  |EEE  | 33|                           | |    |
|EEE  |  $33|    |DDD  |AAA  | 99|                           EEE   FFF
|FFF  |  $34|    |DDD  |FFF  |  5|                                  |
|GGG  |  $44|    |DDD  |EEE  | 44|                                  |
+-----------+    |FFF  |GGG  |  5|                                 GGG
                 +---------------+
Figure 862, Recursive Hierarchy - Tables and Layout






      AAA              << Balanced hierarchy                AAA
       |                  Unbalanced hierarchy >>            |
 +-----+-----+                                           +---+----+
 |     |     |                                           |   |    |
BBB   CCC   DDD                                          |  CCC  DDD
 |     |     |                                           |   |    |
 |     |   +-+-+                                         | +-+  +-+-+
 |     |   |   |                                         | |    |   |
EEE   FFF GGG HHH                                        FFF   GGG HHH
                                                                    |
                                                                    |
                                                                   III
Figure 863, Balanced and Unbalanced Hierarchies






TROUBLE                                                   AAA <------+
+---------+                                                |         |
|PKEY|CKEY|                                          +-----+-----+   |
|----|----|                                          |     |     |   |
|AAA |BBB |                                         BBB   CCC   DDD>-+
|AAA |CCC |                                                |     |
|AAA |DDD |                                                +-+ +-+--+
|CCC |EEE |                                                  | |    |
|DDD |AAA |   <===  This row                                 EEE   FFF
|DDD |FFF |   points back to                                        |
|DDD |EEE |   the hierarchy                                         |
|FFF |GGG |   parent.                                              GGG
+---------+
Figure 864, Recursive Hierarchy - Sample Table and Layout






CREATE TABLE trouble
(pkey     CHAR(03)     NOT NULL
,ckey     CHAR(03)     NOT NULL);
       
CREATE UNIQUE INDEX tble_x1 ON trouble (pkey, ckey);
CREATE UNIQUE INDEX tble_x2 ON trouble (ckey, pkey);
       
INSERT INTO trouble VALUES
('AAA','BBB'),
('AAA','CCC'),
('AAA','DDD'),
('CCC','EEE'),
('DDD','AAA'),
('DDD','EEE'),
('DDD','FFF'),
('FFF','GGG');
Figure 865, Sample Table DDL - Recursive Hierarchy






WITH parent (pkey, ckey, lvl) AS           ANSWER          TROUBLE
  (SELECT DISTINCT                         =============   +---------+
          pkey                             PKEY CKEY LVL   |PKEY|CKEY|
         ,pkey                             ---- ---- ---   |----|----|
         ,0                                AAA  AAA    0   |AAA |BBB |
   FROM   trouble                          AAA  BBB    1   |AAA |CCC |
   WHERE  pkey = 'AAA'                     AAA  CCC    1   |AAA |DDD |
   UNION ALL                               AAA  DDD    1   |CCC |EEE |
   SELECT C.pkey                           CCC  EEE    2   |DDD |AAA |
         ,C.ckey                           DDD  AAA    2   |DDD |FFF |
         ,P.lvl + 1                        DDD  EEE    2   |DDD |EEE |
   FROM   trouble  C                       DDD  FFF    2   |FFF |GGG |
         ,parent   P                       AAA  BBB    3   +---------+
   WHERE  P.ckey      = C.pkey             AAA  CCC    3
     AND  P.lvl + 1 < 4                    AAA  DDD    3
  )                                        FFF  GGG    3
SELECT *
FROM   parent;
Figure 866, Stop Recursive SQL after "n" levels






CREATE FUNCTION LOCATE_BLOCK(searchstr VARCHAR(30000)
                            ,lookinstr VARCHAR(30000))
RETURNS INTEGER
BEGIN ATOMIC
   DECLARE lookinlen, searchlen INT;
   DECLARE locatevar, returnvar INT DEFAULT 0;
   DECLARE beginlook            INT DEFAULT 1;
   SET lookinlen = LENGTH(lookinstr);
   SET searchlen = LENGTH(searchstr);
   WHILE locatevar  = 0         AND
         beginlook <= lookinlen DO
      SET locatevar = LOCATE(searchstr,SUBSTR(lookinstr
                                             ,beginlook
                                             ,searchlen));
      SET beginlook = beginlook + searchlen;
      SET returnvar = returnvar + 1;
   END WHILE;
   IF locatevar = 0 THEN
      SET returnvar = 0;
   END IF;
   RETURN returnvar;
END    
Figure 867, LOCATE_BLOCK user defined function






SELECT id                                            ANSWER
      ,name                                          =================
      ,LOCATE('th',name)       AS l1                 ID  NAME    L1 L2
      ,LOCATE_BLOCK('th',name) AS l2                 --- ------- -- --
FROM   staff                                          70 Rothman  3  2
WHERE  LOCATE('th',name) > 1;                        220 Smith    4  0
Figure 868, LOCATE_BLOCK function example






WITH parent (pkey, ckey, lvl, path, loop) AS
  (SELECT DISTINCT
          pkey
         ,pkey                         ANSWER
         ,0                            ===============================
         ,VARCHAR(pkey,20)             PKEY CKEY LVL PATH         LOOP
         ,0                            ---- ---- --- ------------ ----
   FROM   trouble                      AAA  AAA    0 AAA             0
   WHERE  pkey = 'AAA'                 AAA  BBB    1 AAABBB          0
   UNION ALL                           AAA  CCC    1 AAACCC          0
   SELECT C.pkey                       AAA  DDD    1 AAADDD          0
         ,C.ckey                       CCC  EEE    2 AAACCCEEE       0
         ,P.lvl + 1                    DDD  AAA    2 AAADDDAAA       1
         ,P.path || C.ckey             DDD  EEE    2 AAADDDEEE       0
         ,LOCATE_BLOCK(C.ckey,P.path)  DDD  FFF    2 AAADDDFFF       0
   FROM   trouble C                    AAA  BBB    3 AAADDDAAABBB    0
         ,parent  P                    AAA  CCC    3 AAADDDAAACCC    0
   WHERE  P.ckey      = C.pkey         AAA  DDD    3 AAADDDAAADDD    2
     AND  P.lvl + 1 < 4                FFF  GGG    3 AAADDDFFFGGG    0
  )    
SELECT *
FROM   parent;                         TROUBLE
                                       +---------+        AAA <------+
                                       |PKEY|CKEY|         |         |
                                       |----|----|   +-----+-----+   |
                                       |AAA |BBB |   |     |     |   |
                                       |AAA |CCC |  BBB   CCC   DDD>-+
                                       |AAA |DDD |         |     |
                                       |CCC |EEE |         +-+ +-+--+
                      This row  ===>   |DDD |AAA |           | |    |
                      points back to   |DDD |FFF |           EEE   FFF
                      the hierarchy    |DDD |EEE |                  |
                      parent.          |FFF |GGG |                  |
                                       +---------+                 GGG
Figure 869, Show path, and rows in loop






WITH parent (pkey, ckey, lvl, path) AS      ANSWER
  (SELECT DISTINCT                          ==========================
          pkey                              PKEY CKEY LVL PATH
         ,pkey                              ---- ----- -- ------------
         ,0                                 AAA  AAA    0 AAA
         ,VARCHAR(pkey,20)                  AAA  BBB    1 AAABBB
   FROM   trouble                           AAA  CCC    1 AAACCC
   WHERE  pkey = 'AAA'                      AAA  DDD    1 AAADDD
   UNION ALL                                CCC  EEE    2 AAACCCEEE
   SELECT C.pkey                            DDD  EEE    2 AAADDDEEE
         ,C.ckey                            DDD  FFF    2 AAADDDFFF
         ,P.lvl + 1                         FFF  GGG    3 AAADDDFFFGGG
         ,P.path || C.ckey
   FROM   trouble C
         ,parent  P
   WHERE  P.ckey                      = C.pkey
     AND  LOCATE_BLOCK(C.ckey,P.path) = 0
  )    
SELECT *
FROM   parent;
Figure 870, Use LOCATE_BLOCK function to stop recursion






WITH parent (pkey, ckey, lvl, path, loop) AS
  (SELECT DISTINCT
          pkey
         ,pkey
         ,0
         ,VARCHAR(pkey,20)             ANSWER
         ,0                            ===============================
   FROM   trouble                      PKEY CKEY LVL PATH         LOOP
   WHERE  pkey = 'AAA'                 ---- ---- --- ------------ ----
   UNION ALL                           AAA  AAA    0 AAA             0
   SELECT C.pkey                       AAA  BBB    1 AAABBB          0
         ,C.ckey                       AAA  CCC    1 AAACCC          0
         ,P.lvl + 1                    AAA  DDD    1 AAADDD          0
         ,P.path || C.ckey             CCC  EEE    2 AAACCCEEE       0
         ,LOCATE_BLOCK(C.ckey,P.path)  DDD  AAA    2 AAADDDAAA       1
   FROM   trouble C                    DDD  EEE    2 AAADDDEEE       0
         ,parent  P                    DDD  FFF    2 AAADDDFFF       0
   WHERE  P.ckey = C.pkey              FFF  GGG    3 AAADDDFFFGGG    0
     AND  P.loop = 0
  )    
SELECT *
FROM   parent;
Figure 871, Use LOCATE_BLOCK function to stop recursion






WITH parent (pkey, ckey, lvl, path, loop) AS                 ANSWER
  (SELECT DISTINCT                                           =========
          pkey                                               PKEY CKEY
         ,pkey                                               ---- ----
         ,0                                                  DDD  AAA
         ,VARCHAR(pkey,20)
         ,0
   FROM   trouble
   WHERE  pkey = 'AAA'
   UNION ALL
   SELECT C.pkey
         ,C.ckey                                           TROUBLE
         ,P.lvl + 1                                        +---------+
         ,P.path || C.ckey                                 |PKEY|CKEY|
         ,LOCATE_BLOCK(C.ckey,P.path)                      |----|----|
   FROM   trouble C                                        |AAA |BBB |
         ,parent  P                                        |AAA |CCC |
   WHERE  P.ckey = C.pkey                                  |AAA |DDD |
     AND  P.loop = 0                                       |CCC |EEE |
  )                                       This row  ===>   |DDD |AAA |
SELECT pkey                               points back to   |DDD |FFF |
      ,ckey                               the hierarchy    |DDD |EEE |
FROM   parent                             parent.          |FFF |GGG |
WHERE  loop > 0;                                           +---------+
Figure 872,List rows that point back to a parent






DECLARE GLOBAL TEMPORARY TABLE SESSION.del_list
(pkey   CHAR(03)   NOT NULL
,ckey   CHAR(03)   NOT NULL)
ON COMMIT PRESERVE ROWS;
       
INSERT INTO SESSION.del_list
WITH parent (pkey, ckey, lvl, path, loop) AS
  (SELECT DISTINCT
          pkey
         ,pkey
         ,0                                                TROUBLE
         ,VARCHAR(pkey,20)                                 +---------+
         ,0                                                |PKEY|CKEY|
   FROM   trouble                                          |----|----|
   WHERE  pkey = 'AAA'                                     |AAA |BBB |
   UNION ALL                                               |AAA |CCC |
   SELECT C.pkey                                           |AAA |DDD |
         ,C.ckey                                           |CCC |EEE |
         ,P.lvl + 1                       This row  ===>   |DDD |AAA |
         ,P.path || C.ckey                points back to   |DDD |FFF |
         ,LOCATE_BLOCK(C.ckey,P.path)     the hierarchy    |DDD |EEE |
   FROM   trouble C                       parent.          |FFF |GGG |
         ,parent  P                                        +---------+
   WHERE  P.ckey = C.pkey
     AND  P.loop = 0                                      AAA <------+
)                                                          |         |
SELECT pkey                                          +-----+-----+   |
      ,ckey                                          |     |     |   |
FROM   parent                                       BBB   CCC   DDD>-+
WHERE  loop > 0;                                           |     |
                                                           +-+ +-+--+
DELETE                                                       | |    |
FROM   trouble                                               EEE   FFF
WHERE  (pkey,ckey) IN                                               |
       (SELECT pkey, ckey                                           |
        FROM   SESSION.del_list);                                  GGG
Figure 873, Delete rows that loop back to a parent






CREATE TRIGGER TBL_INS                                     TROUBLE
NO CASCADE BEFORE INSERT ON trouble                        +---------+
REFERENCING NEW AS NNN                       This trigger  |PKEY|CKEY|
FOR EACH ROW MODE DB2SQL                     would reject  |----|----|
   WITH temp (pkey, ckey) AS                 insertion of  |AAA |BBB |
     (VALUES (NNN.pkey                       this row.     |AAA |CCC |
             ,NNN.ckey)                             |      |AAA |DDD |
      UNION ALL                                     |      |CCC |EEE |
      SELECT TTT.pkey                               +--->  |DDD |AAA |
            ,CASE                                          |DDD |FFF |
                WHEN TTT.ckey = TBL.pkey                   |DDD |EEE |
                THEN RAISE_ERROR('70001','LOOP FOUND')     |FFF |GGG |
                ELSE TBL.ckey                              +---------+
             END
      FROM   trouble TBL
            ,temp    TTT
      WHERE  TTT.ckey  = TBL.pkey
     ) 
   SELECT *
   FROM   temp;
Figure 874, INSERT trigger






CREATE TRIGGER TBL_UPD
NO CASCADE BEFORE UPDATE OF pkey, ckey ON trouble
REFERENCING NEW AS NNN
FOR EACH ROW MODE DB2SQL
   WITH temp (pkey, ckey) AS
     (VALUES (NNN.pkey
             ,NNN.ckey)
      UNION ALL
      SELECT TTT.pkey
            ,CASE
                WHEN TTT.ckey = TBL.pkey
                THEN RAISE_ERROR('70001','LOOP FOUND')
                ELSE TBL.ckey
             END
      FROM   trouble TBL
            ,temp    TTT
      WHERE  TTT.ckey  = TBL.pkey
     ) 
   SELECT *
   FROM   temp;
Figure 875, UPDATE trigger






INSERT INTO trouble VALUES('GGG','AAA');
       
UPDATE trouble SET ckey = 'AAA' WHERE pkey = 'FFF';
UPDATE trouble SET pkey = 'GGG' WHERE ckey = 'DDD';
Figure 876, Invalid DML statements






                   HIERARCHY#1                         EXPLODED#1
AAA                +--------------------+              +-------------+
 |                 |KEYY|PKEY|DATA      |              |PKEY|CKEY|LVL|
BBB                |----|----|----------|              |----|----|---|
 |                 |AAA |AAA |SOME DATA |              |AAA |AAA |  0|
 +-----+           |BBB |AAA |MORE DATA |              |AAA |BBB |  1|
 |     |           |CCC |BBB |MORE JUNK |              |AAA |CCC |  2|
CCC   EEE          |DDD |CCC |MORE JUNK |              |AAA |DDD |  3|
 |                 |EEE |BBB |JUNK DATA |              |AAA |EEE |  2|
DDD                +--------------------+              |BBB |BBB |  0|
                                                       |BBB |CCC |  1|
                                                       |BBB |DDD |  2|
                                                       |BBB |EEE |  1|
                                                       |CCC |CCC |  0|
                                                       |CCC |DDD |  1|
                                                       |DDD |DDD |  0|
                                                       |EEE |EEE |  0|
                                                       +-------------+
Figure 877, Data Hierarchy, with normalized and exploded representations






CREATE TABLE hierarchy#1
(keyy    CHAR(3)  NOT NULL
,pkey    CHAR(3)  NOT NULL
,data    VARCHAR(10)
,CONSTRAINT hierarchy11 PRIMARY KEY(keyy)
,CONSTRAINT hierarchy12 FOREIGN KEY(pkey)
 REFERENCES hierarchy#1 (keyy) ON DELETE CASCADE);
       
CREATE TRIGGER HIR#1_UPD
NO CASCADE BEFORE UPDATE OF pkey ON hierarchy#1
REFERENCING NEW AS NNN
            OLD AS OOO
FOR EACH ROW MODE DB2SQL
WHEN (NNN.pkey <> OOO.pkey)
   SIGNAL SQLSTATE '70001' ('CAN NOT UPDATE pkey');
Figure 878, Hierarchy table that does not allow updates to PKEY






CREATE TABLE exploded#1
(pkey  CHAR(4)   NOT NULL
,ckey  CHAR(4)   NOT NULL
,lvl  SMALLINT   NOT NULL
,PRIMARY KEY(pkey,ckey));
Figure 879, Exploded table CREATE statement






CREATE TRIGGER EXP#1_DEL
AFTER DELETE ON hierarchy#1
REFERENCING OLD AS OOO
FOR EACH ROW MODE DB2SQL
   DELETE
   FROM   exploded#1
   WHERE  ckey = OOO.keyy;
Figure 880, Trigger to maintain exploded table after delete in hierarchy table






CREATE TRIGGER EXP#1_INS             HIERARCHY#1       EXPLODED#1
AFTER INSERT ON hierarchy#1          +--------------+  +-------------+
REFERENCING NEW AS NNN               |KEYY|PKEY|DATA|  |PKEY|CKEY|LVL|
FOR EACH ROW MODE DB2SQL             |----|----|----|  |----|----|---|
   INSERT                            |AAA |AAA |S...|  |AAA |AAA |  0|
   INTO exploded#1                   |BBB |AAA |M...|  |AAA |BBB |  1|
   WITH temp(pkey, ckey, lvl) AS     |CCC |BBB |M...|  |AAA |CCC |  2|
     (VALUES (NNN.keyy               |DDD |CCC |M...|  |AAA |DDD |  3|
             ,NNN.keyy               |EEE |BBB |J...|  |AAA |EEE |  2|
             ,0)                     +--------------+  |BBB |BBB |  0|
      UNION ALL                                        |BBB |CCC |  1|
      SELECT  N.pkey                                   |BBB |DDD |  2|
             ,NNN.keyy                                 |BBB |EEE |  1|
             ,T.lvl +1                                 |CCC |CCC |  0|
      FROM    temp        T                            |CCC |DDD |  1|
             ,hierarchy#1 N                            |DDD |DDD |  0|
      WHERE   N.keyy  = T.pkey                         |EEE |EEE |  0|
        AND   N.keyy <> N.pkey                         +-------------+
     ) 
   SELECT *
   FROM   temp;
Figure 881, Trigger to maintain exploded table after insert in hierarchy table






SELECT   *
FROM     exploded#1
WHERE    pkey = :host-var
ORDER BY pkey
        ,ckey
        ,lvl;
Figure 882, Querying the exploded table






CREATE TABLE hierarchy#2
(keyy    CHAR(3)  NOT NULL
,pkey    CHAR(3)  NOT NULL
,data    VARCHAR(10)
,CONSTRAINT NO_loopS21 PRIMARY KEY(keyy)
,CONSTRAINT NO_loopS22 FOREIGN KEY(pkey)
 REFERENCES hierarchy#2 (keyy) ON DELETE CASCADE
                               ON UPDATE RESTRICT);
Figure 883, Hierarchy table that allows updates to PKEY






CREATE TRIGGER HIR#2_UPD                              HIERARCHY#2
NO CASCADE BEFORE UPDATE OF pkey ON hierarchy#2       +--------------+
REFERENCING NEW AS NNN                                |KEYY|PKEY|DATA|
            OLD AS OOO                                |----|----|----|
FOR EACH ROW MODE DB2SQL                              |AAA |AAA |S...|
WHEN (NNN.pkey <> OOO.pkey                            |BBB |AAA |M...|
 AND  NNN.pkey <> NNN.keyy)                           |CCC |BBB |M...|
   WITH temp (keyy, pkey) AS                          |DDD |CCC |M...|
     (VALUES (NNN.keyy                                |EEE |BBB |J...|
             ,NNN.pkey)                               +--------------+
      UNION ALL
      SELECT LP2.keyy
            ,CASE
                WHEN LP2.keyy = NNN.keyy
                THEN RAISE_ERROR('70001','LOOP FOUND')
                ELSE LP2.pkey
             END
      FROM   hierarchy#2 LP2
            ,temp       TMP
      WHERE  TMP.pkey  = LP2.keyy
        AND  TMP.keyy <> TMP.pkey
     ) 
   SELECT *
   FROM   temp;
Figure 884, Trigger to check for recursive data structures before update of PKEY






CREATE TABLE exploded#2
(pkey  CHAR(4)   NOT NULL
,ckey  CHAR(4)   NOT NULL
,lvl   SMALLINT  NOT NULL
,PRIMARY KEY(pkey,ckey));
Figure 885, Exploded table CREATE statement






CREATE TRIGGER EXP#2_DEL
AFTER DELETE ON hierarchy#2
REFERENCING OLD AS OOO
FOR EACH ROW MODE DB2SQL
   DELETE
   FROM   exploded#2
   WHERE  ckey = OOO.keyy;
Figure 886, Trigger to maintain exploded table after delete in hierarchy table






CREATE TRIGGER EXP#2_INS             HIERARCHY#2       EXPLODED#2
AFTER INSERT ON hierarchy#2          +--------------+  +-------------+
REFERENCING NEW AS NNN               |KEYY|PKEY|DATA|  |PKEY|CKEY|LVL|
FOR EACH ROW MODE DB2SQL             |----|----|----|  |----|----|---|
   INSERT                            |AAA |AAA |S...|  |AAA |AAA |  0|
   INTO   exploded#2                 |BBB |AAA |M...|  |AAA |BBB |  1|
   WITH temp(pkey, ckey, lvl) AS     |CCC |BBB |M...|  |AAA |CCC |  2|
     (SELECT  NNN.keyy               |DDD |CCC |M...|  |AAA |DDD |  3|
             ,NNN.keyy               |EEE |BBB |J...|  |AAA |EEE |  2|
             ,0                      +--------------+  |BBB |BBB |  0|
      FROM    hierarchy#2                              |BBB |CCC |  1|
      WHERE   keyy = NNN.keyy                          |BBB |DDD |  2|
      UNION ALL                                        |BBB |EEE |  1|
      SELECT  N.pkey                                   |CCC |CCC |  0|
             ,NNN.keyy                                 |CCC |DDD |  1|
             ,T.lvl +1                                 |DDD |DDD |  0|
      FROM    temp       T                             |EEE |EEE |  0|
             ,hierarchy#2 N                            +-------------+
      WHERE   N.keyy  = T.pkey
        AND   N.keyy <> N.pkey
     ) 
   SELECT *
   FROM   temp;
Figure 887, Trigger to maintain exploded table after insert in hierarchy table






CREATE TRIGGER EXP#2_UPD
AFTER UPDATE OF pkey ON hierarchy#2
REFERENCING OLD AS OOO
            NEW AS NNN
FOR EACH ROW MODE DB2SQL
BEGIN ATOMIC
   DELETE
   FROM   exploded#2
   WHERE  ckey IN
         (SELECT ckey
          FROM   exploded#2
          WHERE  pkey = OOO.keyy);
   INSERT
   INTO   exploded#2
   WITH temp1(ckey) AS
     (VALUES (NNN.keyy)
      UNION ALL
      SELECT  N.keyy
      FROM    temp1      T
             ,hierarchy#2 N
      WHERE   N.pkey  = T.ckey
        AND   N.pkey <> N.keyy
     ) 
Figure 888, Trigger to run after update of PKEY in hierarchy table (part 1 of 2)






   ,temp2(pkey, ckey, lvl) AS
     (SELECT  ckey
             ,ckey
             ,0
      FROM    temp1
      UNION ALL
      SELECT  N.pkey
             ,T.ckey
             ,T.lvl +1
      FROM    temp2      T
             ,hierarchy#2 N
      WHERE   N.keyy  = T.pkey
        AND   N.keyy <> N.pkey
     ) 
   SELECT *
   FROM   temp2;
END    
Figure 889, Trigger to run after update of PKEY in hierarchy table (part 2 of 2)






SELECT   *
FROM     exploded#2
WHERE    pkey = :host-var
ORDER BY pkey
        ,ckey
        ,lvl;
Figure 890, Querying the exploded table






Figure 891, Create Trigger syntax






CREATE TABLE cust_balance
(cust#       INTEGER          NOT NULL
             GENERATED ALWAYS AS IDENTITY
,status      CHAR(2)          NOT NULL
,balance     DECIMAL(18,2)    NOT NULL
,num_trans   INTEGER          NOT NULL
,cur_ts      TIMESTAMP        NOT NULL
,PRIMARY KEY (cust#));
       
CREATE TABLE cust_history
(cust#       INTEGER          NOT NULL
,trans#      INTEGER          NOT NULL
,balance     DECIMAL(18,2)    NOT NULL
,bgn_ts      TIMESTAMP        NOT NULL
,end_ts      TIMESTAMP        NOT NULL
,PRIMARY KEY (cust#, bgn_ts));
       
CREATE TABLE cust_trans
(min_cust#   INTEGER
,max_cust#   INTEGER
,rows_tot    INTEGER          NOT NULL
,change_val  DECIMAL(18,2)
,change_type CHAR(1)          NOT NULL
,cur_ts      TIMESTAMP        NOT NULL
,PRIMARY KEY (cur_ts));
Figure 892, Sample Tables






CREATE TRIGGER cust_bal_ins1
NO CASCADE BEFORE INSERT
ON cust_balance
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
   SET nnn.cur_ts    = CURRENT TIMESTAMP
      ,nnn.num_trans = 1;
Figure 893, Before insert trigger - set values






CREATE TRIGGER cust_bal_upd1
NO CASCADE BEFORE UPDATE
ON cust_balance
REFERENCING NEW AS nnn
            OLD AS ooo
FOR EACH ROW
MODE DB2SQL
   SET nnn.cur_ts    = CURRENT TIMESTAMP
      ,nnn.num_trans = ooo.num_trans + 1;
Figure 894, Before update trigger - set values






CREATE TRIGGER cust_bal_upd2
NO CASCADE BEFORE UPDATE OF balance
ON cust_balance
REFERENCING NEW AS nnn
            OLD AS ooo
FOR EACH ROW
MODE DB2SQL
WHEN (ooo.balance - nnn.balance > 1000)
   SIGNAL SQLSTATE VALUE '71001'
          SET MESSAGE_TEXT = 'Cannot withdraw > 1000';
Figure 895, Before Trigger - flag error






CREATE TRIGGER cust_his_ins1
AFTER INSERT
ON cust_balance
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
   INSERT INTO cust_history VALUES
   (nnn.cust#
   ,nnn.num_trans
   ,nnn.balance
   ,nnn.cur_ts
   ,'9999-12-31-24.00.00');
Figure 896, After Trigger - record insert






CREATE TRIGGER cust_his_upd1
AFTER UPDATE
ON cust_balance
REFERENCING OLD AS ooo
            NEW AS nnn
FOR EACH ROW
MODE DB2SQL
BEGIN ATOMIC
   UPDATE cust_history
   SET    end_ts  =  CURRENT TIMESTAMP
   WHERE  cust#   =  ooo.cust#
     AND  bgn_ts  =  ooo.cur_ts;
   INSERT INTO cust_history VALUES
   (nnn.cust#
   ,nnn.num_trans
   ,nnn.balance
   ,nnn.cur_ts
   ,'9999-12-31-24.00.00');
END    
Figure 897, After Trigger - record update






CREATE TRIGGER cust_his_del1
AFTER DELETE
ON cust_balance
REFERENCING OLD AS ooo
FOR EACH ROW
MODE DB2SQL
   UPDATE cust_history
   SET    end_ts  =  CURRENT TIMESTAMP
   WHERE  cust#   =  ooo.cust#
     AND  bgn_ts  =  ooo.cur_ts;
Figure 898, After Trigger - record delete






CREATE TRIGGER trans_his_ins1
AFTER INSERT
ON cust_balance
REFERENCING NEW_TABLE AS newtab
FOR EACH STATEMENT
MODE DB2SQL
   INSERT INTO cust_trans
   SELECT  MIN(cust#)
          ,MAX(cust#)
          ,COUNT(*)
          ,SUM(balance)
          ,'I'
          ,CURRENT TIMESTAMP
   FROM    newtab;
Figure 899, After Trigger - record insert






CREATE TRIGGER trans_his_upd1
AFTER UPDATE
ON cust_balance
REFERENCING OLD_TABLE AS oldtab
            NEW_TABLE AS newtab
FOR EACH STATEMENT
MODE DB2SQL
   INSERT INTO cust_trans
   SELECT  MIN(nt.cust#)
          ,MAX(nt.cust#)
          ,COUNT(*)
          ,SUM(nt.balance - ot.balance)
          ,'U'
          ,CURRENT TIMESTAMP
   FROM    oldtab ot
          ,newtab nt
   WHERE   ot.cust# = nt.cust#;
Figure 900, After Trigger - record update






CREATE TRIGGER trans_his_del1
AFTER DELETE
ON cust_balance
REFERENCING OLD_TABLE AS oldtab
FOR EACH STATEMENT
MODE DB2SQL
   INSERT INTO cust_trans
   SELECT  MIN(cust#)
          ,MAX(cust#)
          ,COUNT(*)
          ,SUM(balance)
          ,'D'
          ,CURRENT TIMESTAMP
   FROM    oldtab;
Figure 901, After Trigger - record delete






INSERT INTO cust_balance (status, balance) VALUES ('C',123.45);
INSERT INTO cust_balance (status, balance) VALUES ('C',000.00);
INSERT INTO cust_balance (status, balance) VALUES ('D', -1.00);
       
UPDATE cust_balance
SET    balance = balance + 123
WHERE  cust#  <= 2;
       
UPDATE cust_balance
SET    balance = balance * -1
WHERE  cust#   = -1;
       
UPDATE cust_balance
SET    balance = balance - 123
WHERE  cust#   = 1;
       
DELETE 
FROM   cust_balance
WHERE  cust# = 3;
Figure 902, Sample DML statements






Figure 903, Customer-balance table rows






Figure 904, Customer-history table rows






Figure 905, Customer-transaction table rows






CREATE TABLE customer_balance
(cust_id             INTEGER
,cust_name           VARCHAR(20)
,cust_sex            CHAR(1)
,num_sales           SMALLINT
,total_sales         DECIMAL(12,2)
,master_cust_id      INTEGER
,cust_insert_ts      TIMESTAMP
,cust_update_ts      TIMESTAMP);
       
CREATE TABLE us_sales
(invoice#            INTEGER
,cust_id             INTEGER
,sale_value          DECIMAL(18,2)
,sale_insert_ts      TIMESTAMP
,sale_update_ts      TIMESTAMP);
Figure 906, Sample application tables






CREATE DISTINCT TYPE us_dollars AS decimal(18,2) WITH COMPARISONS;
Figure 907, Create US-dollars data type






CREATE TABLE customer_balance
(cust_id             INTEGER        NOT NULL
                     GENERATED ALWAYS AS IDENTITY
                        (START WITH 1
                        ,INCREMENT BY 1
                        ,NO CYCLE
                        ,NO CACHE)
,cust_name           VARCHAR(20)    NOT NULL
,cust_sex            CHAR(1)        NOT NULL
,num_sales           SMALLINT       NOT NULL
,total_sales         us_dollars     NOT NULL
,master_cust_id      INTEGER
,cust_insert_ts      TIMESTAMP      NOT NULL
,cust_update_ts      TIMESTAMP      NOT NULL
,PRIMARY KEY         (cust_id)
,CONSTRAINT c1 CHECK (cust_name   <> '')
,CONSTRAINT c2 CHECK (cust_sex     = 'F'
                  OR  cust_sex     = 'M')
,CONSTRAINT c3 FOREIGN KEY (master_cust_id)
               REFERENCES customer_balance (cust_id)
               ON DELETE CASCADE);
Figure 908, Customer-Balance table DDL






CREATE TABLE us_sales
(invoice#            INTEGER        NOT NULL
,cust_id             INTEGER        NOT NULL
,sale_value          us_dollars     NOT NULL
,sale_insert_ts      TIMESTAMP      NOT NULL
,sale_update_ts      TIMESTAMP      NOT NULL
                                    GENERATED ALWAYS
                                    FOR EACH ROW ON UPDATE
                                    AS ROW CHANGE TIMESTAMP
,PRIMARY KEY         (invoice#)
,CONSTRAINT u1 CHECK (sale_value > us_dollars(0))
,CONSTRAINT u2 FOREIGN KEY (cust_id)
               REFERENCES customer_balance
               ON DELETE RESTRICT);
COMMIT;
       
CREATE INDEX us_sales_cust ON us_sales (cust_id);
Figure 909, US-Sales table DDL






SELECT   *
FROM     us_sales
WHERE    sale_update_ts <= CURRENT TIMESTAMP;
Figure 910, Select run after multi-row insert






SELECT   ROW CHANGE TIMESTAMP FOR us_sales
FROM     us_sales
WHERE    invoice# = 5;
Figure 911, Row change timestamp usage






UPDATE   us_sales
SET      sale_value = DECIMAL(sale_value) + 1
WHERE    invoice#   = 5
  AND    ROW CHANGE TIMESTAMP for us_sales = '2007-11-10-01.02.03';
Figure 912, Update that checks for intervening updates






CREATE TRIGGER cust_balance_ins1
NO CASCADE BEFORE INSERT
ON customer_balance
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
SET nnn.num_sales      = 0
   ,nnn.total_sales    = 0
   ,nnn.cust_insert_ts = CURRENT TIMESTAMP
   ,nnn.cust_update_ts = CURRENT TIMESTAMP;
Figure 913, Set values during insert






CREATE TRIGGER cust_balance_upd1
NO CASCADE BEFORE UPDATE OF cust_update_ts
ON customer_balance
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
SET nnn.cust_update_ts = CURRENT TIMESTAMP;
Figure 914, Set update-timestamp during update






CREATE TRIGGER cust_balance_upd2
NO CASCADE BEFORE UPDATE OF cust_insert_ts
ON customer_balance
FOR EACH ROW
MODE DB2SQL
SIGNAL SQLSTATE VALUE '71001'
       SET MESSAGE_TEXT = 'Cannot update CUST insert-ts';
Figure 915, Prevent update of insert-timestamp






CREATE TRIGGER cust_balance_upd3
NO CASCADE BEFORE UPDATE OF num_sales, total_sales
ON customer_balance
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
WHEN (CURRENT TIMESTAMP >
     (SELECT  MAX(sss.sale_update_ts)
      FROM    us_sales sss
      WHERE   nnn.cust_id = sss.cust_id))
SIGNAL SQLSTATE VALUE '71001'
       SET MESSAGE_TEXT = 'Feilds only updated via US-Sales';
Figure 916, Prevent update of sales fields






CREATE SEQUENCE us_sales_seq
   AS INTEGER
   START WITH 1
   INCREMENT BY 1
   NO CYCLE
   NO CACHE
   ORDER;
Figure 917, Define sequence






CREATE TRIGGER us_sales_ins1
NO CASCADE BEFORE INSERT
ON us_sales
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
SET nnn.invoice#       = NEXTVAL FOR us_sales_seq
   ,nnn.sale_insert_ts = CURRENT TIMESTAMP;
Figure 918, Insert trigger






CREATE TRIGGER sales_to_cust_ins1
AFTER INSERT
ON us_sales
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
UPDATE customer_balance ccc
SET    ccc.num_sales      = ccc.num_sales + 1
      ,ccc.total_sales    = DECIMAL(ccc.total_sales) +
                            DECIMAL(nnn.sale_value)
WHERE  ccc.cust_id        = nnn.cust_id;
Figure 919, Propagate change to Customer-Balance table






CREATE TRIGGER us_sales_upd2
NO CASCADE BEFORE UPDATE OF cust_id, sale_insert_ts
ON us_sales
FOR EACH ROW
MODE DB2SQL
SIGNAL SQLSTATE VALUE '71001'
       SET MESSAGE_TEXT = 'Can only update sale_value';
Figure 920, Prevent updates to selected columns






CREATE TRIGGER sales_to_cust_upd1
AFTER UPDATE OF sale_value
ON us_sales
REFERENCING NEW AS nnn
            OLD AS ooo
FOR EACH ROW
MODE DB2SQL
UPDATE customer_balance ccc
   SET ccc.total_sales = DECIMAL(ccc.total_sales) -
                         DECIMAL(ooo.sale_value)  +
                         DECIMAL(nnn.sale_value)
WHERE  ccc.cust_id     = nnn.cust_id;
Figure 921, Propagate change to Customer-Balance table






CREATE TABLE customer
(cust#        INTEGER       NOT NULL
,cust_name    CHAR(10)
,cust_mgr     CHAR(10)
,PRIMARY KEY(cust#));
Figure 922, Customer table






CREATE TABLE customer_his
(cust#        INTEGER       NOT NULL
,cust_name    CHAR(10)
,cust_mgr     CHAR(10)
,cur_ts       TIMESTAMP     NOT NULL
,cur_actn     CHAR(1)       NOT NULL
,cur_user     VARCHAR(10)   NOT NULL
,prv_cust#    INTEGER
,prv_ts       TIMESTAMP
,PRIMARY KEY(cust#,cur_ts));
       
CREATE UNIQUE INDEX customer_his_x1 ON customer_his
(cust#, prv_ts, cur_ts);
Figure 923, Customer-history table






CREATE TRIGGER customer_ins
AFTER  
INSERT ON customer
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
   INSERT INTO customer_his VALUES
   (nnn.cust#
   ,nnn.cust_name
   ,nnn.cust_mgr
   ,CURRENT TIMESTAMP
   ,'I'
   ,USER
   ,NULL
   ,NULL);
Figure 924, Insert trigger






CREATE TRIGGER customer_upd
AFTER  
UPDATE ON customer
REFERENCING NEW AS nnn
            OLD AS ooo
FOR EACH ROW
MODE DB2SQL
   INSERT INTO customer_his VALUES
   (nnn.cust#
   ,nnn.cust_name
   ,nnn.cust_mgr
   ,CURRENT TIMESTAMP
   ,'U'
   ,USER
   ,ooo.cust#
   ,(SELECT MAX(cur_ts)
     FROM   customer_his hhh
     WHERE  ooo.cust# = hhh.cust#));
Figure 925, Update trigger






CREATE TRIGGER customer_del
AFTER  
DELETE ON customer
REFERENCING OLD AS ooo
FOR EACH ROW
MODE DB2SQL
   INSERT INTO customer_his VALUES
   (ooo.cust#
   ,NULL
   ,NULL
   ,CURRENT TIMESTAMP
   ,'D'
   ,USER
   ,ooo.cust#
   ,(SELECT MAX(cur_ts)
     FROM   customer_his hhh
     WHERE  ooo.cust# = hhh.cust#));
Figure 926, Delete trigger






CREATE TABLE profile
(user_id      VARCHAR(10)   NOT NULL
,bgn_ts       TIMESTAMP     NOT NULL DEFAULT '9999-12-31-24.00.00'
,PRIMARY KEY(user_id));
Figure 927, Profile table






CREATE VIEW customer_vw AS
SELECT  hhh.*
       ,ppp.bgn_ts
FROM    customer_his hhh
       ,profile      ppp
WHERE   ppp.user_id   = USER
  AND   hhh.cur_ts   <= ppp.bgn_ts
  AND   hhh.cur_actn <> 'D'
  AND   NOT EXISTS
       (SELECT *
        FROM   customer_his nnn
        WHERE  nnn.prv_cust# = hhh.cust#
          AND  nnn.prv_ts    = hhh.cur_ts
          AND  nnn.cur_ts   <= ppp.bgn_ts);
Figure 928, View of Customer history






CREATE TABLE version
(vrsn         INTEGER       NOT NULL
,vrsn_bgn_ts  TIMESTAMP     NOT NULL
,CONSTRAINT version1 CHECK(vrsn >= 0)
,CONSTRAINT version2 CHECK(vrsn  < 1000000000)
,PRIMARY KEY(vrsn));
Figure 929, Version table






CREATE TABLE profile
(user_id      VARCHAR(10)   NOT NULL
,vrsn         INTEGER       NOT NULL
,vrsn_bgn_ts  TIMESTAMP     NOT NULL
,CONSTRAINT profile1 FOREIGN KEY(vrsn)
                     REFERENCES version(vrsn)
                     ON DELETE RESTRICT
,PRIMARY KEY(user_id));
Figure 930, Profile table






CREATE TABLE customer_his
(cust#        INTEGER       NOT NULL
,cust_name    CHAR(10)      NOT NULL
,cust_mgr     CHAR(10)
,cur_ts       TIMESTAMP     NOT NULL
,cur_vrsn     INTEGER       NOT NULL
,cur_actn     CHAR(1)       NOT NULL
,cur_user     VARCHAR(10)   NOT NULL
,prv_cust#    INTEGER
,prv_ts       TIMESTAMP
,prv_vrsn     INTEGER
,CONSTRAINT customer1 FOREIGN KEY(cur_vrsn)
                      REFERENCES version(vrsn)
                      ON DELETE RESTRICT
,CONSTRAINT customer2 CHECK(cur_actn IN ('I','U','D'))
,PRIMARY KEY(cust#,cur_vrsn,cur_ts));
       
CREATE INDEX customer_x2 ON customer_his
(prv_cust#
,prv_ts
,prv_vrsn);
Figure 931, Customer table






CREATE VIEW customer_vw AS
SELECT  *
FROM    customer_his hhh
       ,profile      ppp
WHERE   ppp.user_id    =  USER
  AND   hhh.cur_actn  <> 'D'
  AND ((ppp.vrsn       =  0
  AND   hhh.cur_vrsn   =  0)
   OR  (ppp.vrsn       >  0
  AND   hhh.cur_vrsn   =  0
  AND   hhh.cur_ts     <  ppp.vrsn_bgn_ts)
   OR  (ppp.vrsn       >  0
  AND   hhh.cur_vrsn   =  ppp.vrsn))
  AND   NOT EXISTS
       (SELECT *
        FROM    customer_his nnn
        WHERE   nnn.prv_cust#  =  hhh.cust#
          AND   nnn.prv_ts     =  hhh.cur_ts
          AND   nnn.prv_vrsn   =  hhh.cur_vrsn
          AND ((ppp.vrsn       =  0
          AND   nnn.cur_vrsn   =  0)
           OR  (ppp.vrsn       >  0
          AND   nnn.cur_vrsn   =  0
          AND   nnn.cur_ts     <  ppp.vrsn_bgn_ts)
           OR  (ppp.vrsn       >  0
          AND   nnn.cur_vrsn   =  ppp.vrsn)));
Figure 932, Customer view - 1 of 2






CREATE VIEW customer AS
SELECT  cust#
       ,cust_name
       ,cust_mgr
FROM    customer_vw;
Figure 933, Customer view - 2 of 2






CREATE TRIGGER customer_ins
INSTEAD OF
INSERT ON customer_vw
REFERENCING NEW AS nnn
FOR EACH ROW
MODE DB2SQL
   INSERT INTO customer_his VALUES
   (nnn.cust#
   ,nnn.cust_name
   ,nnn.cust_mgr
   ,CURRENT TIMESTAMP
   ,(SELECT vrsn
     FROM   profile
     WHERE  user_id = USER)
   ,CASE
       WHEN 0 < (SELECT COUNT(*)
                 FROM   customer
                 WHERE  cust# = nnn.cust#)
       THEN RAISE_ERROR('71001','ERROR: Duplicate cust#')
       ELSE 'I'
    END
   ,USER
   ,NULL
   ,NULL
   ,NULL);
Figure 934, Insert trigger






CREATE TRIGGER customer_upd
INSTEAD OF
UPDATE ON customer_vw
REFERENCING NEW AS nnn
            OLD AS ooo
FOR EACH ROW
MODE DB2SQL
   INSERT INTO customer_his VALUES
   (nnn.cust#
   ,nnn.cust_name
   ,nnn.cust_mgr
   ,CURRENT TIMESTAMP
   ,ooo.vrsn
   ,CASE
       WHEN nnn.cust# <> ooo.cust#
       THEN RAISE_ERROR('72001','ERROR: Cannot change cust#')
       ELSE 'U'
    END
   ,ooo.user_id
   ,ooo.cust#
   ,ooo.cur_ts
   ,ooo.cur_vrsn);
Figure 935, Update trigger






CREATE TRIGGER customer_del
INSTEAD OF
DELETE ON customer_vw
REFERENCING OLD AS ooo
FOR EACH ROW
MODE DB2SQL
   INSERT INTO customer_his VALUES
   (ooo.cust#
   ,ooo.cust_name
   ,ooo.cust_mgr
   ,CURRENT TIMESTAMP
   ,ooo.vrsn
   ,'D'
   ,ooo.user_id
   ,ooo.cust#
   ,ooo.cur_ts
   ,ooo.cur_vrsn);
Figure 936, Delete trigger






SELECT  'SELECT COUNT(*) FROM ' CONCAT
         RTRIM(tabschema)       CONCAT
        '.'                     CONCAT
         tabname                CONCAT
        ';'
FROM     syscat.tables
WHERE    tabschema    = 'SYSCAT'
  AND    tabname   LIKE 'N%'
ORDER BY tabschema                                              ANSWER
        ,tabname;            =========================================
                             SELECT COUNT(*) FROM SYSCAT.NAMEMAPPINGS;
                             SELECT COUNT(*) FROM SYSCAT.NODEGROUPDEF;
                             SELECT COUNT(*) FROM SYSCAT.NODEGROUPS;
Figure 937, Generate SQL to count rows






EXPORT TO C:\FRED.TXT OF DEL
MODIFIED BY NOCHARDEL
SELECT  'SELECT COUNT(*) FROM ' CONCAT
         RTRIM(tabschema)       CONCAT
        '.'                     CONCAT
         tabname                CONCAT
        ';'
FROM     syscat.tables
WHERE    tabschema    = 'SYSCAT'
  AND    tabname   LIKE 'N%'
ORDER BY tabschema
        ,tabname;
Figure 938, Export generated SQL statements






SELECT  'SELECT  '''            CONCAT
         tabname                CONCAT
         ''', COUNT(*) FROM '   CONCAT
         RTRIM(tabschema)       CONCAT
        '.'                     CONCAT
         tabname                CONCAT
        ';'
FROM     syscat.tables
WHERE    tabschema    = 'SYSCAT'
  AND    tabname   LIKE 'N%'
ORDER BY tabschema
        ,tabname;
                                                                ANSWER
            ==========================================================
            SELECT  'NAMEMAPPINGS', COUNT(*) FROM SYSCAT.NAMEMAPPINGS;
            SELECT  'NODEGROUPDEF', COUNT(*) FROM SYSCAT.NODEGROUPDEF;
            SELECT  'NODEGROUPS', COUNT(*) FROM SYSCAT.NODEGROUPS;
Figure 939, Generate SQL to count rows






WITH temp1 (num) AS
   (VALUES (1),(2),(3),(4))
SELECT   CASE num
            WHEN 1 THEN 'SELECT '''
                     || tabname
                     || ''' AS tname'
            WHEN 2 THEN '       ,COUNT(*)'
                     || ' AS #rows'
            WHEN 3 THEN 'FROM    '
                     || RTRIM(tabschema)
                     || '.'                                     ANSWER
                     || tabname         ==============================
                     || ';'             SELECT 'NAMEMAPPINGS' AS tname
            WHEN 4 THEN ''                     ,COUNT(*) AS #rows
         END                            FROM    SYSCAT.NAMEMAPPINGS;
FROM     syscat.tables
        ,temp1                          SELECT 'NODEGROUPDEF' AS tname
WHERE    tabschema    = 'SYSCAT'               ,COUNT(*) AS #rows
  AND    tabname   LIKE 'N%'            FROM    SYSCAT.NODEGROUPDEF;
ORDER BY tabschema
        ,tabname                        SELECT 'NODEGROUPS' AS tname
        ,num;                                  ,COUNT(*) AS #rows
                                        FROM    SYSCAT.NODEGROUPS;
Figure 940, Generate SQL to count rows






WITH temp1 (num) AS
   (VALUES (1),(2),(3),(4))
SELECT   CASE num
            WHEN 1 THEN  'SELECT SUM(C1)'
            when 2 then  'FROM ('
            WHEN 3 THEN  '   SELECT COUNT(*) AS C1 FROM '  CONCAT
                          RTRIM(tabschema)                 CONCAT
                         '.'                               CONCAT
                          tabname                          CONCAT
                   CASE dd
                      WHEN 1 THEN  ''
                      ELSE         ' UNION ALL'
                   END
            WHEN 4 THEN ') AS xxx;'
         END
FROM    (SELECT   tab.*
                 ,ROW_NUMBER() OVER(ORDER BY tabschema ASC
                                            ,tabname   ASC)  AS aa
                 ,ROW_NUMBER() OVER(ORDER BY tabschema DESC
                                            ,tabname   DESC) AS dd
         FROM     syscat.tables tab
         WHERE    tabschema    = 'SYSCAT'
           AND    tabname   LIKE 'N%'
        )AS xxx
        ,temp1
WHERE   (num <= 2  AND  aa = 1)
   OR   (num  = 3)
   OR   (num  = 4  AND  dd = 1)
ORDER BY tabschema ASC
        ,tabname   ASC
        ,num       ASC;
                                                                ANSWER
           ===========================================================
           SELECT SUM(C1)
           FROM (
              SELECT COUNT(*) AS C1 FROM SYSCAT.NAMEMAPPINGS UNION ALL
              SELECT COUNT(*) AS C1 FROM SYSCAT.NODEGROUPDEF UNION ALL
              SELECT COUNT(*) AS C1 FROM SYSCAT.NODEGROUPS
           ) AS xxx;
Figure 941, Generate SQL to count rows (all tables)






SELECT   empno
        ,lastname
        ,workdept
        ,salary
FROM     employee
WHERE    empno  = '000250';
Figure 942, Sample query






SELECT   all-columns
FROM     all-relevant-tables
WHERE    all-predicates-are-true
Figure 943, Sample pseudo-query






SELECT   COUNT(*)
FROM     all-relevant-tables
WHERE    empno  = '000250';
Figure 944, Sample pseudo-query






SELECT   CHAR(tabname,15)   AS tabname
        ,get_INTEGER(
            ' SELECT  COUNT(*)'  ||
            ' FROM '  || tabschema  || '.' || tabname ||
            ' WHERE ' || colname    || ' = ''000250'''
         ) AS num_rows
FROM     syscat.columns
WHERE    tabschema  =   USER                                    ANSWER
  AND    colname    =  'EMPNO'                    ====================
  AND    typename   =  'CHARACTER'                TABNAME     NUM_ROWS
ORDER BY tabname;                                 ----------- --------
                                                  EMP_PHOTO          0
                                                  VEMP               1
                                                  VEMPDPT1           1
                                                  VEMPPROJACT        9
                                                  VSTAFAC2           9
Figure 945, Count matching rows in all matching tables






SELECT   all-columns
FROM     all-relevant-tables
WHERE    empno  = '000250';
Figure 946, Sample pseudo-query






WITH temp1 AS
   (SELECT   tabname
            ,VARCHAR(
               ' SELECT  *'  ||
               ' FROM '  || tabschema  || '.' || tabname ||
               ' WHERE ' || colname    || ' = ''000250'''
             ) AS SQL_text
   FROM     syscat.columns
   WHERE    tabschema  =   USER
     AND    colname    =  'EMPNO'
     AND    typename   =  'CHARACTER'
   )   
SELECT   CHAR(t1.tabname,10)   AS tabname
        ,t2.row_number         AS row#
        ,t2.col_num            AS col#
        ,CHAR(t2.col_name,15)  AS colname
        ,CHAR(t2.col_type,15)  AS coltype
        ,CHAR(t2.col_value,20) AS colvalue
FROM     temp1 t1
        ,TABLE(tab_transpose(sql_text)) AS t2
ORDER BY t1.tabname
        ,t2.row_number
        ,t2.col_num;
Figure 947, Select all matching columns/rows in all matching tables






TABNAME     ROW#   COL#  COLNAME    COLTYPE   COLVALUE
----------  -----  ----  ---------  --------  ----------
EMPLOYEE        1     1  EMPNO      CHAR      000250
EMPLOYEE        1     2  FIRSTNME   VARCHAR   DANIEL
EMPLOYEE        1     3  MIDINIT    CHAR      S
EMPLOYEE        1     4  LASTNAME   VARCHAR   SMITH
EMPLOYEE        1     5  WORKDEPT   CHAR      D21
EMPLOYEE        1     6  PHONENO    CHAR      0961
EMPLOYEE        1     7  HIREDATE   DATE      1999-10-30
EMPLOYEE        1     8  JOB        CHAR      CLERK
EMPLOYEE        1     9  EDLEVEL    SMALLINT  15
EMPLOYEE        1    10  SEX        CHAR      M
EMPLOYEE        1    11  BIRTHDATE  DATE      1969-11-12
EMPLOYEE        1    12  SALARY     DECIMAL   49180.00
EMPLOYEE        1    13  BONUS      DECIMAL   400.00
EMPLOYEE        1    14  COMM       DECIMAL   1534.00
EMPPROJACT      1     1  EMPNO      CHAR      000250
EMPPROJACT      1     2  PROJNO     CHAR      AD3112
EMPPROJACT      1     3  ACTNO      SMALLINT  60
EMPPROJACT      1     4  EMPTIME    DECIMAL   1.00
EMPPROJACT      1     5  EMSTDATE   DATE      2002-01-01
EMPPROJACT      1     6  EMENDATE   DATE      2002-02-01
EMPPROJACT      2     1  EMPNO      CHAR      000250
EMPPROJACT      2     2  PROJNO     CHAR      AD3112
EMPPROJACT      2     3  ACTNO      SMALLINT  60
EMPPROJACT      2     4  EMPTIME    DECIMAL   0.50
EMPPROJACT      2     5  EMSTDATE   DATE      2002-02-01
EMPPROJACT      2     6  EMENDATE   DATE      2002-03-15
Figure 948, Transpose query output






SELECT   CHAR(tabschema,8)  AS schema
        ,CHAR(tabname,20)   AS tabname
        ,return_INTEGER
         ('SELECT  COUNT(*) '  ||
          'FROM ' || tabschema || '.' || tabname
         )AS #rows
FROM     syscat.tables
WHERE    tabschema    = 'SYSCAT'                                ANSWER
  AND    tabname   LIKE 'RO%'             ============================
ORDER BY tabschema                        SCHEMA TABNAME         #ROWS
        ,tabname                          ------ --------------- -----
FOR FETCH ONLY                            SYSCAT ROUTINEAUTH       168
WITH UR;                                  SYSCAT ROUTINEDEP         41
                                          SYSCAT ROUTINEPARMS     2035
                                          SYSCAT ROUTINES          314
Figure 949, List tables, and count rows in same






CREATE FUNCTION return_INTEGER (in_stmt VARCHAR(4000))
RETURNS INTEGER
LANGUAGE SQL
READS SQL DATA
NO EXTERNAL ACTION
BEGIN ATOMIC
   DECLARE out_val INTEGER;
   CALL    return_INTEGER(in_stmt,out_val);
   RETURN  out_val;
END    
Figure 950, return_INTEGER function






CREATE PROCEDURE return_INTEGER (IN  in_stmt VARCHAR(4000)
                                ,OUT out_val INTEGER)
LANGUAGE SQL
READS SQL DATA
NO EXTERNAL ACTION
BEGIN  
   DECLARE c1 CURSOR FOR s1;
   PREPARE s1 FROM in_stmt;
   OPEN    c1;
   FETCH   c1 INTO out_val;
   CLOSE   c1;
   RETURN;
END    
Figure 951, return_INTEGER stored procedure






CREATE PROCEDURE return_DECIMAL (IN  in_stmt VARCHAR(4000)
                                ,OUT out_val DECIMAL(31,6))
LANGUAGE SQL
READS SQL DATA
NO EXTERNAL ACTION
BEGIN  
   DECLARE c1 CURSOR FOR s1;
   PREPARE s1 FROM in_stmt;
   OPEN    c1;
   FETCH   c1 INTO out_val;
   CLOSE   c1;
   RETURN;
END    
Figure 952, return_DECIMAL function






CREATE FUNCTION return_DECIMAL (in_stmt VARCHAR(4000))
RETURNS DECIMAL(31,6)
LANGUAGE SQL
READS SQL DATA
NO EXTERNAL ACTION
BEGIN ATOMIC
   DECLARE out_val DECIMAL(31,6);
   CALL    return_DECIMAL(in_stmt,out_val);
   RETURN  out_val;
END    
Figure 953, return_DECIMAL stored procedure






SELECT   CHAR(tabschema,8)  AS schema
        ,CHAR(tabname,20)   AS tabname
        ,#rows
FROM    (SELECT   tabschema
                 ,tabname
                 ,return_INTEGER(
                     ' SELECT  COUNT(*)'  ||
                     ' FROM ' || tabschema || '.' || tabname ||
                     ' FOR FETCH ONLY WITH UR'
                  ) AS #rows
         FROM     syscat.tables tab
         WHERE    tabschema  LIKE  'SYS%'
           AND    type          =  'T'
           AND    stats_time   IS  NULL
        )AS xxx
WHERE    #rows > 1000                                           ANSWER
ORDER BY #rows DESC                       ============================
FOR FETCH ONLY                            SCHEMA TABNAME         #ROWS
WITH UR;                                  ------ --------------- -----
                                          SYSIBM SYSCOLUMNS       3518
                                          SYSIBM SYSROUTINEPARMS  2035
Figure 954, List tables never had RUNSTATS






SELECT   CHAR(tab.tabname,15)   AS tabname
        ,CHAR(col.colname,10)   AS colname
        ,CHAR(COALESCE(return_VARCHAR(
            ' SELECT ''Y'''  ||
            ' FROM '  || tab.tabschema  || '.' || tab.tabname ||
            ' WHERE ' || col.colname    || ' = ''A00''' ||
            ' FETCH FIRST 1 ROWS ONLY ' ||
            ' OPTIMIZE FOR 1 ROW ' ||
            ' WITH UR'
         ),'N'),1) AS has_dept
FROM     syscat.columns col
        ,syscat.tables  tab
WHERE    col.tabschema  =  USER
  AND    col.colname   IN ('DEPTNO','WORKDEPT')
  AND    col.tabschema  =  tab.tabschema
  AND    col.tabname    =  tab.tabname
  AND    tab.type       =  'T'
FOR FETCH ONLY
WITH UR;                                                        ANSWER
                                         =============================
                                         TABNAME    COLNAME   HAS_DEPT
                                         ---------- --------- --------
                                         DEPARTMENT DEPTNO    Y
                                         EMPLOYEE   WORKDEPT  Y
                                         PROJECT    DEPTNO    N
Figure 955, List tables with a row for A00 department






SELECT   CHAR(tab.tabname,15)   AS tabname
        ,CHAR(col.colname,10)   AS colname
        ,CHAR(COALESCE(return_VARCHAR(
            ' SELECT ''Y'''  ||
            ' FROM '  || tab.tabschema  || '.' || tab.tabname ||
            ' WHERE ' || col.colname    || ' = ''A00''' ||
            ' FETCH FIRST 1 ROWS ONLY ' ||
            ' OPTIMIZE FOR 1 ROW ' ||
            ' WITH UR'
         ),'N'),1) AS has_dept
FROM     syscat.columns col
        ,syscat.tables  tab
WHERE    col.tabschema  =  USER
  AND    col.colname   IN ('DEPTNO','WORKDEPT')
  AND    col.tabschema  =  tab.tabschema
  AND    col.tabname    =  tab.tabname
  AND    tab.type       =  'T'
  AND    col.colname   IN
        (SELECT SUBSTR(idx.colnames,2,LENGTH(col.colname))
         FROM   syscat.indexes idx
         WHERE  tab.tabschema = idx.tabschema
           AND  tab.tabname   = idx.tabname)
FOR FETCH ONLY
WITH UR;                                                        ANSWER
                                           ===========================
                                           TABNAME    COLNAME HAS_DEPT
                                           ---------- ------- --------
                                           DEPARTMENT DEPTNO  Y
Figure 956, List suitably-indexed tables with a row for A00 department






CREATE FUNCTION get_Integer(VARCHAR(4000))
RETURNS INTEGER
LANGUAGE JAVA
EXTERNAL NAME 'Graeme2!get_Integer'
PARAMETER STYLE DB2GENERAL
NO EXTERNAL ACTION
NOT DETERMINISTIC
READS SQL DATA
FENCED;
Figure 957, CREATE FUNCTION code






import java.lang.*;
import COM.ibm.db2.app.*;
import java.sql.*;
import java.math.*;
import java.io.*;
       
public class Graeme2 extends UDF {
  public void get_Integer(String inStmt,
                          int    outValue)
  throws Exception {
    try {
      Connection         con = DriverManager.getConnection
                               ("jdbc:default:connection");
      PreparedStatement stmt = con.prepareStatement(inStmt);
      ResultSet           rs = stmt.executeQuery();
      if (rs.next() == true  &&  rs.getString(1) != null) {
        set(2, rs.getInt(1));
      }
      rs.close();
      stmt.close();
      con.close();
    }  
    catch (SQLException sqle) {
      setSQLstate("38999");
      setSQLmessage("SQLCODE = " + sqle.getSQLState());
      return;
    }  
  }    
}      
Figure 958, CREATE FUNCTION java code






                                                                ANSWER
                                            ==========================
                                            DEPT EMPNO  SALARY   #ROWS
SELECT   workdept AS dept                   ---- ------ -------- -----
        ,empno                              E11  000290 35340.00     7
        ,salary                             E21  200330 35370.00     6
        ,get_Integer(                       E21  200340 31840.00     6
            ' SELECT count(*)'     ||
            ' FROM employee'       ||
            ' where workdept = ''' || workdept || ''' ')
            AS #rows
FROM     employee
WHERE    salary  < 35500
ORDER BY workdept
        ,empno;
Figure 959, Java function usage example






CREATE FUNCTION tab_Varchar (VARCHAR(4000))
RETURNS TABLE  (row_number  INTEGER
               ,row_value   VARCHAR(254))
LANGUAGE JAVA
EXTERNAL NAME 'Graeme2!tab_Varchar'
PARAMETER STYLE DB2GENERAL
NO EXTERNAL ACTION
NOT DETERMINISTIC
DISALLOW PARALLEL
READS SQL DATA
FINAL CALL
FENCED;
Figure 960, CREATE FUNCTION code






import java.lang.*;
import COM.ibm.db2.app.*;
import java.sql.*;
import java.math.*;
import java.io.*;
       
public class Graeme2 extends UDF {
  Connection        con;
  Statement         stmt;
  ResultSet         rs;
  int               rowNum;
  public void tab_Varchar(String inStmt,
                          int    outNumber,
                          String outValue)
  throws Exception {
    switch (getCallType()) {
      case SQLUDF_TF_FIRST:
        break;
      case SQLUDF_TF_OPEN:
        rowNum = 1;
        try {
          con  = DriverManager.getConnection
                 ("jdbc:default:connection");
          stmt = con.createStatement();
          rs   = stmt.executeQuery(inStmt);
        }
        catch(SQLException sqle) {
          setSQLstate("38999");
          setSQLmessage("SQLCODE = " + sqle.getSQLState());
          return;
        }
        break;
      case SQLUDF_TF_FETCH:
        if (rs.next() == true) {
          set(2, rowNum);
          if (rs.getString(1) != null) {
            set(3, rs.getString(1));
          }
          rowNum++;
        }
        else {
          setSQLstate ("02000");
        }
        break;
      case SQLUDF_TF_CLOSE:
        rs.close();
        stmt.close();
        con.close();
        break;
      case SQLUDF_TF_FINAL:
        break;
    }  
  }    
}      
Figure 961, CREATE FUNCTION java code






WITH   
make_queries AS
  (SELECT   tab.tabschema
           ,tab.tabname
           ,' SELECT EMPNO ' ||
            ' FROM '     || tab.tabschema || '.' || tab.tabname
             AS sql_text
   FROM     syscat.tables  tab
           ,syscat.columns col
   WHERE    tab.tabschema  =  USER
     AND    tab.type       =  'T'
     AND    col.tabschema  =  tab.tabschema
     AND    col.tabname    =  tab.tabname
     AND    col.colname    =  'EMPNO'
     AND    col.typename   =  'CHARACTER'
     AND    col.length     =   6
   ),  
run_queries AS
   (SELECT   qqq.*
            ,ttt.*
    FROM     make_queries qqq
            ,TABLE(tab_Varchar(sql_text)) AS ttt
   )   
SELECT   CHAR(row_value,10)                   AS empno
        ,COUNT(*)                             AS #rows
        ,COUNT(DISTINCT tabschema || tabname) AS #tabs
        ,CHAR(MIN(tabname),18)                AS min_tab
        ,CHAR(MAX(tabname),18)                AS max_tab
FROM     run_queries
GROUP BY row_value
HAVING   COUNT(DISTINCT tabschema || tabname) > 3
ORDER BY row_value
FOR FETCH ONLY
WITH UR;                                                        ANSWER
                                ======================================
                                EMPNO  #ROWS#TABS MIN_TAB   MAX_TAB
                                ------ ---- ----- --------- ----------
                                000130    7     4 EMP_PHOTO EMPPROJACT
                                000140   10     4 EMP_PHOTO EMPPROJACT
                                000150    7     4 EMP_PHOTO EMPPROJACT
                                000190    7     4 EMP_PHOTO EMPPROJACT
Figure 962, Use Tabular Function






SELECT   all columns
FROM     unknown tables
WHERE    any unknown columns = '%ABC%'
Figure 963, Cool query pseudo-code






SELECT   *
FROM     empprojact
WHERE    empno  = '000150';
                                                                ANSWER
                     =================================================
                     EMPNO  PROJNO ACTNO EMPTIME EMSTDATE   EMENDATE
                     ------ ------ ----- ------- ---------- ----------
                     000150 MA2112    60    1.00 01/01/2002 07/15/2002
                     000150 MA2112   180    1.00 07/15/2002 02/01/2003
Figure 964, Select rows






SELECT   SMALLINT(row_number)         AS row#
        ,col_num                      AS col#
        ,CHAR(col_name,13)            AS col_name
        ,CHAR(col_type,10)            AS col_type
        ,col_length                   AS col_len
        ,SMALLINT(LENGTH(col_value))  AS val_len
        ,SUBSTR(col_value,1,20)       AS col_value
FROM     TABLE(tab_Transpose(
            ' SELECT   *'                    ||
            ' FROM     empprojact'           ||
            ' WHERE    empno  = ''000150'''
         )) AS ttt
ORDER BY 1,2;                                                   ANSWER
                ======================================================
                ROW# COL# COL_NAME COL_TYPE COL_LEN VAL_LEN COL_VALUE
                ---- ---- -------- -------- ------- ------- ----------
                   1    1 EMPNO    CHAR           6       6 000150
                   1    2 PROJNO   CHAR           6       6 MA2112
                   1    3 ACTNO    SMALLINT       6       2 60
                   1    4 EMPTIME  DECIMAL        7       4 1.00
                   1    5 EMSTDATE DATE          10      10 2002-01-01
                   1    6 EMENDATE DATE          10      10 2002-07-15
                   2    1 EMPNO    CHAR           6       6 000150
                   2    2 PROJNO   CHAR           6       6 MA2112
                   2    3 ACTNO    SMALLINT       6       3 180
                   2    4 EMPTIME  DECIMAL        7       4 1.00
                   2    5 EMSTDATE DATE          10      10 2002-07-15
                   2    6 EMENDATE DATE          10      10 2003-02-01
Figure 965, Select rows ?then transpose






WITH   
make_queries AS
  (SELECT   tab.tabschema
           ,tab.tabname
           ,' SELECT   *' ||
            ' FROM '      || tab.tabname ||
            ' WHERE empno  = ''000150'''
             AS sql_text
   FROM     syscat.tables  tab
           ,syscat.columns col
   WHERE    tab.tabschema  =  USER
     AND    tab.type       =  'T'
     AND    col.tabschema  =  tab.tabschema
     AND    col.tabname    =  tab.tabname
     AND    col.colname    =  'EMPNO'
     AND    col.typename   =  'CHARACTER'
     AND    col.length     =   6
   ),  
run_queries AS
   (SELECT   qqq.*
            ,ttt.*
    FROM     make_queries qqq
            ,TABLE(tab_Transpose(sql_text)) AS ttt
   )   
SELECT   SUBSTR(tabname,1,11)         AS tab_name
        ,SMALLINT(row_number)         AS row#
        ,col_num                      AS col#
        ,CHAR(col_name,13)            AS col_name
        ,CHAR(col_type,10)            AS col_type
        ,col_length                   AS col_len
        ,SMALLINT(LENGTH(col_value))  AS val_len
        ,SUBSTR(col_value,1,20)       AS col_value
FROM     run_queries
ORDER BY 1,2,3;
Figure 966, Select rows in any table ?then transpose






TAB_NAME   ROW# COL# COL_NAME      COL_TYPE COL_LEN VAL_LEN COL_VALUE
---------- ---- ---- ------------- -------- ------- -------  ---------
EMP_PHOTO     1    1 EMPNO         CHAR           6       6 000150
EMP_PHOTO     1    2 PHOTO_FORMAT  VARCHAR       10       6 bitmap
EMP_PHOTO     1    3 PICTURE       BLOB      204800       - -
EMP_PHOTO     1    4 EMP_ROWID     CHAR          40      40
EMP_PHOTO     2    1 EMPNO         CHAR           6       6 000150
EMP_PHOTO     2    2 PHOTO_FORMAT  VARCHAR       10       3 gif
EMP_PHOTO     2    3 PICTURE       BLOB      204800       - -
EMP_PHOTO     2    4 EMP_ROWID     CHAR          40      40
EMP_RESUME    1    1 EMPNO         CHAR           6       6 000150
EMP_RESUME    1    2 RESUME_FORMAT VARCHAR       10       5 ascii
EMP_RESUME    1    3 RESUME        CLOB        5120       - -
EMP_RESUME    1    4 EMP_ROWID     CHAR          40      40
EMP_RESUME    2    1 EMPNO         CHAR           6       6 000150
EMP_RESUME    2    2 RESUME_FORMAT VARCHAR       10       4 html
EMP_RESUME    2    3 RESUME        CLOB        5120       - -
EMP_RESUME    2    4 EMP_ROWID     CHAR          40      40
EMPLOYEE      1    1 EMPNO         CHAR           6       6 000150
EMPLOYEE      1    2 FIRSTNME      VARCHAR       12       5 BRUCE
EMPLOYEE      1    3 MIDINIT       CHAR           1       1
EMPLOYEE      1    4 LASTNAME      VARCHAR       15       7 ADAMSON
EMPLOYEE      1    5 WORKDEPT      CHAR           3       3 D11
EMPLOYEE      1    6 PHONENO       CHAR           4       4 4510
EMPLOYEE      1    7 HIREDATE      DATE          10      10 2002-02-12
EMPLOYEE      1    8 JOB           CHAR           8       8 DESIGNER
EMPLOYEE      1    9 EDLEVEL       SMALLINT       6       2 16
EMPLOYEE      1   10 SEX           CHAR           1       1 M
EMPLOYEE      1   11 BIRTHDATE     DATE          10      10 1977-05-17
EMPLOYEE      1   12 SALARY        DECIMAL       11       8 55280.00
EMPLOYEE      1   13 BONUS         DECIMAL       11       6 500.00
EMPLOYEE      1   14 COMM          DECIMAL       11       7 2022.00
EMPPROJACT    1    1 EMPNO         CHAR           6       6 000150
EMPPROJACT    1    2 PROJNO        CHAR           6       6 MA2112
EMPPROJACT    1    3 ACTNO         SMALLINT       6       2 60
EMPPROJACT    1    4 EMPTIME       DECIMAL        7       4 1.00
EMPPROJACT    1    5 EMSTDATE      DATE          10      10 2002-01-01
EMPPROJACT    1    6 EMENDATE      DATE          10      10 2002-07-15
EMPPROJACT    2    1 EMPNO         CHAR           6       6 000150
EMPPROJACT    2    2 PROJNO        CHAR           6       6 MA2112
EMPPROJACT    2    3 ACTNO         SMALLINT       6       3 180
EMPPROJACT    2    4 EMPTIME       DECIMAL        7       4 1.00
EMPPROJACT    2    5 EMSTDATE      DATE          10      10 2002-07-15
EMPPROJACT    2    6 EMENDATE      DATE          10      10 2003-02-01
Figure 967, Select rows in any table ?answer






WITH   
search_values (search_type,search_length,search_value) AS
  (VALUES     ('CHARACTER',6,'000150')
  ),   
       
list_columns AS
  (SELECT   val.search_value
           ,tab.tabschema
           ,tab.tabname
           ,col.colname
           ,ROW_NUMBER() OVER(PARTITION BY val.search_value
                                          ,tab.tabschema
                                          ,tab.tabname
                              ORDER BY     col.colname ASC)  AS col_a
           ,ROW_NUMBER() OVER(PARTITION BY val.search_value
                                          ,tab.tabschema
                                          ,tab.tabname
                              ORDER BY     col.colname DESC) AS col_d
   FROM     search_values  val
           ,syscat.tables  tab
           ,syscat.columns col
   WHERE    tab.tabschema  =  USER
     AND    tab.type       =  'T'
     AND    tab.tabschema  =  col.tabschema
     AND    tab.tabname    =  col.tabname
     AND    col.typename   =  val.search_type
     AND    col.length     =  val.search_length
   ),  
       
make_queries (search_value
             ,tabschema
             ,tabname
             ,colname
             ,col_a
             ,col_d
             ,sql_text) AS
   (SELECT   tb1.*
            ,VARCHAR(' SELECT *' ||
                     ' FROM '    || tabname ||
                     ' WHERE '   || colname || ' = ''' ||
                                    search_value  || ''''
                     ,4000)
    FROM     list_columns tb1
    WHERE    col_a  = 1
    UNION ALL
    SELECT   tb2.*
            ,mqy.sql_text ||
             ' OR '  || tb2.colname   ||
             ' = ''' || tb2.search_value  || ''''
    FROM     list_columns tb2
            ,make_queries mqy
    WHERE    tb2.search_value  =  mqy.search_value
      AND    tb2.tabschema     =  mqy.tabschema
      AND    tb2.tabname       =  mqy.tabname
      AND    tb2.col_a         =  mqy.col_a + 1
   ),  
       
run_queries AS
   (SELECT   qqq.*
            ,ttt.*
    FROM     make_queries qqq
            ,TABLE(tab_Transpose_4K(sql_text)) AS ttt
    WHERE    col_d = 1
   )   
Figure 968, Select rows in any table ?then transpose (part 1 of 2)






SELECT   SUBSTR(tabname,1,11)         AS tab_name
        ,SMALLINT(row_number)         AS row#
        ,col_num                      AS col#
        ,CHAR(col_name,13)            AS col_name
        ,CHAR(col_type,10)            AS col_type
        ,col_length                   AS col_len
        ,SMALLINT(LENGTH(col_value))  AS val_len
        ,SUBSTR(col_value,1,20)       AS col_value
FROM     run_queries
ORDER BY 1,2,3;
Figure 969, Select rows in any table ?then transpose (part 2 of 2)






TAB_NAME   ROW# COL# COL_NAME      COL_TYPE COL_LEN VAL_LEN COL_VALUE
---------- ---- ---- ------------- -------- ------- -------  ---------
EMP_PHOTO     1    1 EMPNO         CHAR           6       6 000150
EMP_PHOTO     1    2 PHOTO_FORMAT  VARCHAR       10       6 bitmap
EMP_PHOTO     1    3 PICTURE       BLOB      204800       - -
EMP_PHOTO     1    4 EMP_ROWID     CHAR          40      40
EMP_PHOTO     2    1 EMPNO         CHAR           6       6 000150
EMP_PHOTO     2    2 PHOTO_FORMAT  VARCHAR       10       3 gif
EMP_PHOTO     2    3 PICTURE       BLOB      204800       - -
EMP_PHOTO     2    4 EMP_ROWID     CHAR          40      40
EMP_RESUME    1    1 EMPNO         CHAR           6       6 000150
EMP_RESUME    1    2 RESUME_FORMAT VARCHAR       10       5 ascii
EMP_RESUME    1    3 RESUME        CLOB        5120       - -
EMP_RESUME    1    4 EMP_ROWID     CHAR          40      40
EMP_RESUME    2    1 EMPNO         CHAR           6       6 000150
EMP_RESUME    2    2 RESUME_FORMAT VARCHAR       10       4 html
EMP_RESUME    2    3 RESUME        CLOB        5120       - -
EMP_RESUME    2    4 EMP_ROWID     CHAR          40      40
EMPLOYEE      1    1 EMPNO         CHAR           6       6 000150
EMPLOYEE      1    2 FIRSTNME      VARCHAR       12       5 BRUCE
EMPLOYEE      1    3 MIDINIT       CHAR           1       1
EMPLOYEE      1    4 LASTNAME      VARCHAR       15       7 ADAMSON
EMPLOYEE      1    5 WORKDEPT      CHAR           3       3 D11
EMPLOYEE      1    6 PHONENO       CHAR           4       4 4510
EMPLOYEE      1    7 HIREDATE      DATE          10      10 2002-02-12
EMPLOYEE      1    8 JOB           CHAR           8       8 DESIGNER
EMPLOYEE      1    9 EDLEVEL       SMALLINT       6       2 16
EMPLOYEE      1   10 SEX           CHAR           1       1 M
EMPLOYEE      1   11 BIRTHDATE     DATE          10      10 1977-05-17
EMPLOYEE      1   12 SALARY        DECIMAL       11       8 55280.00
EMPLOYEE      1   13 BONUS         DECIMAL       11       6 500.00
EMPLOYEE      1   14 COMM          DECIMAL       11       7 2022.00
EMPPROJACT    1    1 EMPNO         CHAR           6       6 000150
EMPPROJACT    1    2 PROJNO        CHAR           6       6 MA2112
EMPPROJACT    1    3 ACTNO         SMALLINT       6       2 60
EMPPROJACT    1    4 EMPTIME       DECIMAL        7       4 1.00
EMPPROJACT    1    5 EMSTDATE      DATE          10      10 2002-01-01
EMPPROJACT    1    6 EMENDATE      DATE          10      10 2002-07-15
EMPPROJACT    2    1 EMPNO         CHAR           6       6 000150
EMPPROJACT    2    2 PROJNO        CHAR           6       6 MA2112
EMPPROJACT    2    3 ACTNO         SMALLINT       6       3 180
EMPPROJACT    2    4 EMPTIME       DECIMAL        7       4 1.00
EMPPROJACT    2    5 EMSTDATE      DATE          10      10 2002-07-15
EMPPROJACT    2    6 EMENDATE      DATE          10      10 2003-02-01
PROJECT       1    1 PROJNO        CHAR           6       6 MA2112
PROJECT       1    2 PROJNAME      VARCHAR       24      16 W L ROBOT
PROJECT       1    3 DEPTNO        CHAR           3       3 D11
PROJECT       1    4 RESPEMP       CHAR           6       6 000150
PROJECT       1    5 PRSTAFF       DECIMAL        7       4 3.00
PROJECT       1    6 PRSTDATE      DATE          10      10 2002-01-01
PROJECT       1    7 PRENDATE      DATE          10      10 1982-12-01
PROJECT       1    8 MAJPROJ       CHAR           6       6 MA2110
Figure 970, Select rows in any table ?answer






SELECT * FROM ACT WHERE ACTKWD = '000150'
SELECT * FROM DEPARTMENT WHERE MGRNO = '000150'
SELECT * FROM EMP_PHOTO WHERE EMPNO = '000150'
SELECT * FROM EMP_RESUME WHERE EMPNO = '000150'
SELECT * FROM EMPLOYEE WHERE EMPNO = '000150'
SELECT * FROM EXPLAIN_OPERATOR WHERE OPERATOR_TYPE = '000150'
SELECT * FROM PROJACT WHERE PROJNO = '000150'
SELECT * FROM EMPPROJACT WHERE EMPNO = '000150' OR PROJNO = '000150'
SELECT * FROM PROJECT WHERE MAJPROJ = '000150' OR PROJNO = '000150' OR
 RESPEMP = '000150'
Figure 971, Queries generated above






CREATE FUNCTION tab_Transpose (VARCHAR(4000))
RETURNS TABLE  (row_number     INTEGER
               ,num_cols       SMALLINT
               ,col_num        SMALLINT
               ,col_name       VARCHAR(128)
               ,col_type       VARCHAR(128)
               ,col_length     INTEGER
               ,col_value      VARCHAR(254))
LANGUAGE JAVA
EXTERNAL NAME 'Graeme2!tab_Transpose'
PARAMETER STYLE DB2GENERAL
NO EXTERNAL ACTION
NOT DETERMINISTIC
DISALLOW PARALLEL
READS SQL DATA
FINAL CALL
FENCED;
Figure 972, Create transpose function






import java.lang.*;
import COM.ibm.db2.app.*;
import java.sql.*;
import java.math.*;
import java.io.*;
       
public class Graeme2 extends UDF {
       
  Connection        con;
  Statement         stmt;
  ResultSet         rs;
  ResultSetMetaData rsmtadta;
  int               rowNum;
  int               i;
  int               outLength;
  short             colNum;
  int               colCount;
  String[] colName  = new String[1100];
  String[] colType  = new String[1100];
  int[]    colSize  = new int[1100];
  public void writeRow()
  throws Exception {
    set(2, rowNum);
    set(3, (short) colCount);
    set(4, colNum);
    set(5, colName[colNum]);
    set(6, colType[colNum]);
Figure 973, CREATE FUNCTION java code (part 1 of 3)






    set(7, colSize[colNum]);
    if (colType[colNum].equals("XML")         ||
        colType[colNum].equals("BLOB")        ||
        colType[colNum].equals("CLOB")        ||
        colType[colNum].equals("DBLOB")       ||
        colType[colNum].equals("GRAPHIC")     ||
        colType[colNum].equals("VARGRAPHIC")  ||
        colSize[colNum] > outLength) {
      // DON'T DISPLAY THIS VALUE
      return;
    }  
    else if (rs.getString(colNum) !=  null) {
      // DISPLAY THIS COLUMN VALUE
      set(8, rs.getString(colNum));
    }  
  }    
       
  public void tab_Transpose(String inStmt
                           ,int    rowNumber
                           ,short  numColumns
                           ,short  outColNumber
                           ,String outColName
                           ,String outColtype
                           ,int    outColSize
                           ,String outColValue)
  throws Exception {
    switch (getCallType()) {
      case SQLUDF_TF_FIRST:
        break;
      case SQLUDF_TF_OPEN:
        try {
          con      = DriverManager.getConnection
                     ("jdbc:default:connection");
          stmt     = con.createStatement();
          rs       = stmt.executeQuery(inStmt);
          // GET COLUMN NAMES
          rsmtadta = rs.getMetaData();
          colCount = rsmtadta.getColumnCount();
          for (i=1; i <= colCount; i++) {
            colName[i] = rsmtadta.getColumnName(i);
            colType[i] = rsmtadta.getColumnTypeName(i);
            colSize[i] = rsmtadta.getColumnDisplaySize(i);
          }
          rowNum    = 1;
          colNum    = 1;
          outLength = 254;
        }
        catch(SQLException sqle) {
          setSQLstate("38999");
          setSQLmessage("SQLCODE = " + sqle.getSQLState());
          return;
        }
        break;
      case SQLUDF_TF_FETCH:
        if (colNum == 1 && rs.next() == true) {
          writeRow();
          colNum++;
          if (colNum > colCount) {
            colNum = 1;
            rowNum++;
          }
        }
Figure 974, CREATE FUNCTION java code (part 2 of 3)






        else if (colNum > 1 && colNum <= colCount) {
          writeRow();
          colNum++;
          if (colNum > colCount) {
            colNum = 1;
            rowNum++;
          }
        }
        else {
          setSQLstate ("02000");
        }
        break;
      case SQLUDF_TF_CLOSE:
        rs.close();
        stmt.close();
        con.close();
        break;
      case SQLUDF_TF_FINAL:
        break;
    }  
}}     
Figure 975, CREATE FUNCTION java code (part 3 of 3)






CREATE PROCEDURE execute_immediate (IN  in_stmt     VARCHAR(1000)
                                   ,OUT out_sqlcode INTEGER)
LANGUAGE SQL
MODIFIES SQL DATA
BEGIN  
   DECLARE sqlcode INTEGER;
   DECLARE EXIT HANDLER FOR sqlexception
       SET out_sqlcode = sqlcode;
   EXECUTE IMMEDIATE in_stmt;
   SET out_sqlcode = sqlcode;
   RETURN;
END!   
       
CREATE FUNCTION execute_immediate (in_stmt VARCHAR(1000))
RETURNS TABLE (sqltext VARCHAR(1000)
              ,sqlcode INTEGER)
LANGUAGE SQL
MODIFIES SQL DATA                                         IMPORTANT
BEGIN ATOMIC                                              ============
   DECLARE out_sqlcode INTEGER;                           This example
   CALL execute_immediate(in_stmt, out_sqlcode);          uses an "!"
   RETURN VALUES (in_stmt, out_sqlcode);                  as the stmt
END!                                                      delimiter.
Figure 976, Define function and stored-procedure






WITH temp1 AS
   (SELECT   tabschema
            ,tabname
    FROM     syscat.tables
    WHERE    tabschema   =  'FRED'
      AND    type        =  'S'
      AND    status      =  'C'
      AND    tabname  LIKE  '%DEPT%'
   )   
SELECT   CHAR(tab.tabname,20)   AS tabname
        ,stm.sqlcode            AS sqlcode
        ,CHAR(stm.sqltext,100)  AS sqltext
FROM     temp1 AS tab
        ,TABLE(execute_immediate(
              'REFRESH TABLE ' ||
               RTRIM(tab.tabschema) || '.' || tab.tabname
         ))AS stm
ORDER BY tab.tabname
WITH UR;
Figure 977, Refresh matching tables






TABNAME      SQLCODE  SQLTEXT
-----------  -------  ------------------------------
STAFF_DEPT1        0  REFRESH TABLE FRED.STAFF_DEPT1
STAFF_DEPT2        0  REFRESH TABLE FRED.STAFF_DEPT2
Figure 978, Refresh matching tables - answer






SELECT   CHAR(tab.tabname,20)   AS tabname
        ,stm.sqlcode            AS sqlcode
        ,CHAR(stm.sqltext,100)  AS sqltext
FROM     syscat.tables AS tab
        ,TABLE(execute_immediate(
             ' CREATE TABLE ' ||
               RTRIM(tab.tabschema) || '.' || tab.tabname  || '_C1' ||
             ' LIKE ' || RTRIM(tab.tabschema) || '.' || tab.tabname
         ))AS stm
WHERE    tab.tabschema     =  USER
  AND    tab.tabname    LIKE  'S%'
ORDER BY tab.tabname
FOR FETCH ONLY
WITH UR;                                                        ANSWER
            ==========================================================
            TABNAME SQLCODE SQLTEXT
            ------- ------- ------------------------------------------
            SALES         0 CREATE TABLE FRED.SALES_C1 LIKE FRED.SALES
            STAFF         0 CREATE TABLE FRED.STAFF_C1 LIKE FRED.STAFF
Figure 979, Create copies of tables - wrong






WITH temp1 AS
  (SELECT  tabschema
          ,tabname
   FROM     syscat.tables
   WHERE    tabschema     =  USER
     AND    tabname    LIKE  'S%'
  )    
SELECT   CHAR(tab.tabname,20)   AS tabname
        ,stm.sqlcode            AS sqlcode
        ,CHAR(stm.sqltext,100)  AS sqltext
FROM     temp1 tab
        ,TABLE(execute_immediate(
             ' CREATE TABLE ' ||
               RTRIM(tab.tabschema) || '.' || tab.tabname  || '_C1' ||
             ' LIKE ' || RTRIM(tab.tabschema) || '.' || tab.tabname
         ))AS stm
ORDER BY tab.tabname
FOR FETCH ONLY
WITH UR;                                                        ANSWER
            ==========================================================
            TABNAME SQLCODE SQLTEXT
            ------- ------- ------------------------------------------
            SALES         0 CREATE TABLE FRED.SALES_C1 LIKE FRED.SALES
            STAFF         0 CREATE TABLE FRED.STAFF_C1 LIKE FRED.STAFF
Figure 980, Create copies of tables - right






WITH   
temp0 AS
   (SELECT   RTRIM(tabschema) AS schema
            ,tabname          AS old_tabname
            ,tabname || '_C2' AS new_tabname
    FROM     syscat.tables
    WHERE    tabschema     =  USER
      AND    tabname    LIKE  'S%'
   ),  
temp1 AS
   (SELECT   tab.*
            ,stm.sqlcode            AS sqlcode1
            ,CHAR(stm.sqltext,200)  AS sqltext1
    FROM     temp0 AS tab
            ,TABLE(execute_immediate(
                  ' CREATE TABLE ' || schema || '.' || new_tabname ||
                  ' LIKE '         || schema || '.' || old_tabname
             ))AS stm
   ),  
temp2 AS
   (SELECT   tab.*
            ,stm.sqlcode            AS sqlcode2
            ,CHAR(stm.sqltext,200)  AS sqltext2
    FROM     temp1 AS tab
            ,TABLE(execute_immediate(
                  ' INSERT   INTO ' || schema || '.' || new_tabname ||
                  ' SELECT * FROM ' || schema || '.' || old_tabname
             ))AS stm
   )   
SELECT   CHAR(old_tabname,20) AS tabname
        ,sqlcode1                                               ANSWER
        ,sqlcode2                            =========================
FROM     temp2                               TABNAME SQLCODE1 SQLCODE2
ORDER BY old_tabname                         ------- -------- --------
FOR FETCH ONLY                               SALES          0        0
WITH UR;                                     STAFF          0        0
Figure 981, Create copies of tables, then populate






FROM     clause
JOIN ON  clause
WHERE    clause
GROUP BY and aggregate
HAVING   clause
SELECT   list
ORDER BY clause
FETCH FIRST
Figure 982, Query Processing Sequence






WITH temp1 (col1) AS                                            ANSWER
(VALUES     0                                                   ======
 UNION ALL                                                      COL1
 SELECT col1 + 1                                                ----
 FROM   temp1                                                      0
 WHERE  col1 + 1 < 100                                             1
)                                                                  2
SELECT *                                                           3
FROM   temp1;                                                    etc
Figure 983, Use recursion to get list of 100 numbers






SELECT  *
FROM    TABLE(NumList(100)) AS xxx;
Figure 984, Use user-defined-function to get list of 100 numbers






WITH temp1 (s1, r1) AS                                    ANSWER
(VALUES (0, RAND(1))                                      ============
 UNION ALL                                                SEQ#    RAN1
 SELECT  s1+1, RAND()                                      ----  -----
 FROM    temp1                                                0  0.001
 WHERE   s1+1 < 5                                             1  0.563
)                                                             2  0.193
SELECT SMALLINT(s1)    AS seq#                                3  0.808
      ,DECIMAL(r1,5,3) AS ran1                                4  0.585
FROM   temp1;
Figure 985, Use RAND to create pseudo-random numbers






WITH temp1 (s1, r1) AS                        ANSWER
(VALUES (0, RAND(2))                          ========================
 UNION ALL                                    SEQ#  RAN2  RAN1    RAN3
 SELECT  s1+1, RAND()                         ----  ----  ------  ----
 FROM    temp1                                   0    13  0.0013     0
 WHERE   s1+1 < 5                                1  8916  0.8916     8
)                                                2  7384  0.7384     7
SELECT SMALLINT(s1)       AS seq#                3  5430  0.5430     5
      ,SMALLINT(r1*10000) AS ran2                4  8998  0.8998     8
      ,DECIMAL(r1,6,4)    AS ran1
      ,SMALLINT(r1*10)    AS ran3
FROM   temp1;
Figure 986, Make differing ranges of random numbers






                                               ANSWER
                                               =======================
                                               S#   RAN1   RAN2   RAN3
WITH                                           -- ------ ------ ------
temp1 (s1) AS                                   0   1251 365370 114753
  (VALUES (0)                                   1 350291 280730  88106
   UNION ALL                                    2 710501 149549 550422
   SELECT s1 + 1                                3 147312  33311   2339
   FROM   temp1                                 4   8911    556  73091
   WHERE  s1 + 1   < 5
  )    
SELECT SMALLINT(s1)                             AS s#
      ,INTEGER((RAND(1))                 * 1E6) AS ran1
      ,INTEGER((RAND() * RAND())         * 1E6) AS ran2
      ,INTEGER((RAND() * RAND()* RAND()) * 1E6) AS ran3
FROM   temp1;
Figure 987, Create RAND data with different distributions






WITH temp1 (s1, r1) AS                             ANSWER
(VALUES (0, RAND(2))                               ===================
 UNION ALL                                         SEQ# RAN2 RAN3 RAN4
 SELECT  s1+1, RAND()                              ---- ---- ---- ----
 FROM    temp1                                        0   65 A    65
 WHERE   s1+1 < 5                                     1   88 X    88
)                                                     2   84 T    84
SELECT SMALLINT(s1)             AS seq#               3   79 O    79
      ,SMALLINT(r1*26+65)       AS ran2               4   88 X    88
      ,CHR(SMALLINT(r1*26+65))  AS ran3
      ,CHAR(SMALLINT(r1*26)+65) AS ran4
FROM   temp1;
Figure 988, Converting RAND output from number to character






CREATE TABLE personnel
(emp#       INTEGER        NOT NULL
,socsec#    CHAR(11)       NOT NULL
,job_ftn    CHAR(4)        NOT NULL
,dept       SMALLINT       NOT NULL
,salary     DECIMAL(7,2)   NOT NULL
,date_bn    DATE           NOT NULL WITH DEFAULT
,fst_name   VARCHAR(20)
,lst_name   VARCHAR(20)
,CONSTRAINT pex1 PRIMARY KEY (emp#)
,CONSTRAINT pe01 CHECK (emp#                   >  0)
,CONSTRAINT pe02 CHECK (LOCATE(' ',socsec#)    =  0)
,CONSTRAINT pe03 CHECK (LOCATE('-',socsec#,1)  =  4)
,CONSTRAINT pe04 CHECK (LOCATE('-',socsec#,5)  =  7)
,CONSTRAINT pe05 CHECK (job_ftn               <> '')
,CONSTRAINT pe06 CHECK (dept    BETWEEN 1 AND    99)
,CONSTRAINT pe07 CHECK (salary  BETWEEN 0 AND 99999)
,CONSTRAINT pe08 CHECK (fst_name              <> '')
,CONSTRAINT pe09 CHECK (lst_name              <> '')
,CONSTRAINT pe10 CHECK (date_bn  >= '1900-01-01'  ));
       
CREATE UNIQUE INDEX PEX2 ON PERSONNEL (SOCSEC#);
CREATE UNIQUE INDEX PEX3 ON PERSONNEL (DEPT, EMP#);
Figure 989, Production-like test table DDL






INSERT INTO personnel
WITH temp1 (s1,r1,r2,r3,r4) AS
   (VALUES (0
           ,RAND(2)
           ,RAND()+(RAND()/1E5)
           ,RAND()* RAND()
           ,RAND()* RAND()* RAND())
    UNION ALL
    SELECT  s1 + 1
           ,RAND()
           ,RAND()+(RAND()/1E5)
           ,RAND()* RAND()
           ,RAND()* RAND()* RAND()
    FROM   temp1
    WHERE  s1  <  10000)
SELECT 100000 + s1
      ,SUBSTR(DIGITS(INT(r2*988+10)),8) || '-' ||
       SUBSTR(DIGITS(INT(r1*88+10)),9)  || '-' ||
       TRANSLATE(SUBSTR(DIGITS(s1),7),'9873450126','0123456789')
      ,CASE
         WHEN INT(r4*9) > 7 THEN 'MGR'
         WHEN INT(r4*9) > 5 THEN 'SUPR'
         WHEN INT(r4*9) > 3 THEN 'PGMR'
         WHEN INT(R4*9) > 1 THEN 'SEC'
         ELSE 'WKR'
       END
      ,INT(r3*98+1)
      ,DECIMAL(r4*99999,7,2)
      ,DATE('1930-01-01') + INT(50-(r4*50)) YEARS
                          + INT(r4*11) MONTHS
                          + INT(r4*27) DAYS
      ,CHR(INT(r1*26+65))|| CHR(INT(r2*26+97))|| CHR(INT(r3*26+97))||
       CHR(INT(r4*26+97))|| CHR(INT(r3*10+97))|| CHR(INT(r3*11+97))
      ,CHR(INT(r2*26+65))||
       TRANSLATE(CHAR(INT(r2*1E7)),'aaeeiibmty','0123456789')
FROM   temp1;
Figure 990, Production-like test table INSERT






EMP#    SOCSEC#     JOB_ DEPT SALARY    DATE_BN    F_NME     L_NME
------  ----------- ---- ---- --------- ---------- --------- ---------
100000  484-10-9999 WKR    47     13.63 1979-01-01 Ammaef    Mimytmbi
100001  449-38-9998 SEC    53  35758.87 1962-04-10 Ilojff    Liiiemea
100002  979-90-9997 WKR     1   8155.23 1975-01-03 Xzacaa    Zytaebma
100003  580-50-9993 WKR    31  16643.50 1971-02-05 Lpiedd    Pimmeeat
100004  264-87-9994 WKR    21    962.87 1979-01-01 Wgfacc    Geimteei
100005  661-84-9995 WKR    19   4648.38 1977-01-02 Wrebbc    Rbiybeet
100006  554-53-9990 WKR     8    375.42 1979-01-01 Mobaaa    Oiiaiaia
100007  482-23-9991 SEC    36  23170.09 1968-03-07 Emjgdd    Mimtmamb
100008  536-41-9992 WKR     6  10514.11 1974-02-03 Jnbcaa    Nieebayt
Figure 991, Production-like test table, Sample Output






CREATE TABLE time_series
(KYY      CHAR(03)     NOT NULL
,bgn_dt   DATE         NOT NULL
,end_dt   DATE         NOT NULL
,CONSTRAINT tsc1 CHECK (kyy <> '')
,CONSTRAINT tsc2 CHECK (bgn_dt <= end_dt));
COMMIT;
       
INSERT INTO TIME_series values
('AAA','1995-10-01','1995-10-04'),
('AAA','1995-10-06','1995-10-06'),
('AAA','1995-10-07','1995-10-07'),
('AAA','1995-10-15','1995-10-19'),
('BBB','1995-10-01','1995-10-01'),
('BBB','1995-10-03','1995-10-03');
Figure 992, Sample Table DDL - Time Series






Figure 993, Overlapping Time-Series rows - Definition







SELECT kyy                                                   ANSWER
      ,bgn_dt                                                =========
      ,end_dt                                                
FROM   time_series a
WHERE  EXISTS
      (SELECT *
       FROM   time_series b
       WHERE  a.kyy     = b.kyy
         AND  a.bgn_dt <> b.bgn_dt
         AND (a.bgn_dt  BETWEEN b.bgn_dt AND b.end_dt
          OR  b.bgn_dt  BETWEEN a.bgn_dt AND a.end_dt))
ORDER BY 1,2;
Figure 994, Find overlapping rows in time-series






SELECT a.kyy                               TIME_SERIES
      ,a.bgn_dt                            +-------------------------+
      ,a.end_dt                            |KYY|BGN_DT    |END_DT    |
      ,b.bgn_dt                            |---|----------|----------|
      ,b.end_dt                            |AAA|1995-10-01|1995-10-04|
      ,DAYS(b.bgn_dt) -                    |AAA|1995-10-06|1995-10-06|
       DAYS(A.end_dt)                      |AAA|1995-10-07|1995-10-07|
         as diff                           |AAA|1995-10-15|1995-10-19|
FROM   time_series a                       |BBB|1995-10-01|1995-10-01|
      ,time_series b                       |BBB|1995-10-03|1995-10-03|
WHERE  a.kyy    = b.kyy                    +-------------------------+
  AND  a.end_dt < b.bgn_dt - 1 DAY
  AND  NOT EXISTS
      (SELECT *
       FROM   time_series z
       WHERE  z.kyy    = a.kyy
         AND  z.kyy    = b.kyy
         AND  z.bgn_dt > a.bgn_dt
         AND  z.bgn_dt < b.bgn_dt)
ORDER BY 1,2;
Figure 995, Find gap in Time-Series, SQL






KEYCOL  BGN_DT      END_DT      BGN_DT      END_DT      DIFF
------  ----------  ----------  ----------  ----------  ----
AAA     1995-10-01  1995-10-04  1995-10-06  1995-10-06     2
AAA     1995-10-07  1995-10-07  1995-10-15  1995-10-19     8
BBB     1995-10-01  1995-10-01  1995-10-03  1995-10-03     2
Figure 996, Find gap in Time-Series, Answer






W
SELECT a.kyy                AS kyy         TIME_SERIES
      ,a.end_dt + 1 DAY     AS bgn_gap     +-------------------------+
      ,b.bgn_dt - 1 DAY     AS end_gap     |KYY|BGN_DT    |END_DT    |
      ,(DAYS(b.bgn_dt) -                   |---|----------|----------|
        DAYS(a.end_dt) - 1) AS sz          |AAA|1995-10-01|1995-10-04|
FROM   time_series a                       |AAA|1995-10-06|1995-10-06|
      ,time_series b                       |AAA|1995-10-07|1995-10-07|
WHERE  a.kyy    = b.kyy                    |AAA|1995-10-15|1995-10-19|
  AND  a.end_dt < b.bgn_dt - 1 DAY         |BBB|1995-10-01|1995-10-01|
  AND  NOT EXISTS                          |BBB|1995-10-03|1995-10-03|
      (SELECT *                            +-------------------------+
       FROM   time_series z
       WHERE  z.kyy    = a.kyy            ANSWER
         AND  z.kyy    = b.kyy            ============================
         AND  z.bgn_dt > a.bgn_dt         KYY BGN_GAP    END_GAP    SZ
         AND  z.bgn_dt < b.bgn_dt)        --- ---------- ---------- --
ORDER BY 1,2;                             AAA 1995-10-05 1995-10-05  1
                                          AAA 1995-10-08 1995-10-14  7
                                          BBB 1995-10-02 1995-10-02  1
Figure 997, Find gap in Time-Series






WITH temp                                  TIME_SERIES
(kyy, gap_dt, gsize) AS                    +-------------------------+
(SELECT a.kyy                              |KYY|BGN_DT    |END_DT    |
       ,a.end_dt + 1 DAY                   |---|----------|----------|
       ,(DAYS(b.bgn_dt) -                  |AAA|1995-10-01|1995-10-04|
         DAYS(a.end_dt) - 1)               |AAA|1995-10-06|1995-10-06|
 FROM   time_series a                      |AAA|1995-10-07|1995-10-07|
       ,time_series b                      |AAA|1995-10-15|1995-10-19|
 WHERE  a.kyy    = b.kyy                   |BBB|1995-10-01|1995-10-01|
   AND  a.end_dt < b.bgn_dt - 1 DAY        |BBB|1995-10-03|1995-10-03|
   AND  NOT EXISTS                         +-------------------------+
       (SELECT *
        FROM   time_series z
        WHERE  z.kyy    = a.kyy
          AND  z.kyy    = b.kyy                ANSWER
          AND  z.bgn_dt > a.bgn_dt             =======================
          AND  z.bgn_dt < b.bgn_dt)            KEYCOL GAP_DT     GSIZE
 UNION ALL                                     ------ ---------- -----
 SELECT kyy                                    AAA    1995-10-05     1
       ,gap_dt + 1 DAY                         AAA    1995-10-08     7
       ,gsize  - 1                             AAA    1995-10-09     6
 FROM   temp                                   AAA    1995-10-10     5
 WHERE  gsize  > 1                             AAA    1995-10-11     4
)                                              AAA    1995-10-12     3
SELECT   *                                     AAA    1995-10-13     2
FROM     temp                                  AAA    1995-10-14     1
ORDER BY 1,2;                                  BBB    1995-10-02     1
Figure 998, Show each day in Time-Series gap






Figure 999, TABLESAMPLE Syntax






SELECT   *
FROM     staff TABLESAMPLE BERNOULLI(5) REPEATABLE(1234)
ORDER BY id;
Figure 1000, Sample rows in STAFF table






SELECT   *
FROM     employee ee TABLESAMPLE BERNOULLI(18)
        ,emp_act  ea TABLESAMPLE BERNOULLI(25)
WHERE    ee.empno = ea.empno
ORDER BY ee.empno;
Figure 1001, Sample rows in two tables






DECLARE GLOBAL TEMPORARY TABLE session.nyc_staff
LIKE    staff;
       
SELECT   *
FROM     session.nyc_staff TABLESAMPLE SYSTEM(34.55)
WHERE    id     < 100
  AND    salary > 100
ORDER BY id;
Figure 1002, Sample Views used in Join Examples






WITH temp1 (c1) AS                   ANSWER (numbers shortened)
(VALUES '123  ',' 345 ','  567')     =================================
SELECT c1                            C1    DBL         DEC   SML  INT
      ,DOUBLE(c1)    AS dbl          ----- ----------- ----- ---- ----
      ,DECIMAL(c1,3) AS dec          123    +1.2300E+2  123.  123  123
      ,SMALLINT(c1)  AS sml           345   +3.4500E+2  345.  345  345
      ,INTEGER(c1)   AS int            567  +5.6700E+2  567.  567  567
FROM   temp1;
Figure 1003, Convert Character to Numeric - SQL






INPUT STRING    COMPATIBLE FUNCTIONS
============    ==========================================
"      1234"    DOUBLE, DECIMAL, INTEGER, SMALLINT, BIGINT
"      12.4"    DOUBLE, DECIMAL
"      12E4"    DOUBLE
Figure 1004, Acceptable conversion values






WITH temp1 (c1) AS (VALUES ' 123','456 ',' 1 2',' 33%',NULL)
SELECT c1
      ,TRANSLATE(c1,'          ','1234567890')                AS c2
      ,LENGTH(LTRIM(TRANSLATE(c1,'          ','1234567890'))) AS c3
FROM   temp1;
                                                                ANSWER
                                                          ============
                                                          C1   C2   C3
                                                          ---- ---- --
                                                           123       0
                                                          456        0
                                                           1 2       0
                                                           33%    %  1
                                                          -    -     -
Figure 1005, Checking for non-digits






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE FUNCTION isnumeric(instr VARCHAR(40))              This example
RETURNS CHAR(1)                                           uses an "!"
BEGIN ATOMIC                                              as the stmt
   DECLARE is_number CHAR(1)  DEFAULT 'Y';                delimiter.
   DECLARE bgn_blank CHAR(1)  DEFAULT 'Y';
   DECLARE found_num CHAR(1)  DEFAULT 'N';
   DECLARE found_pos CHAR(1)  DEFAULT 'N';
   DECLARE found_neg CHAR(1)  DEFAULT 'N';
   DECLARE found_dot CHAR(1)  DEFAULT 'N';
   DECLARE ctr       SMALLINT DEFAULT 1;
   IF instr IS NULL THEN
      RETURN NULL;
   END IF;
   wloop:
   WHILE ctr       <= LENGTH(instr) AND
         is_number  = 'Y'
   DO  
      -----------------------------
      --- ERROR CHECKS          ---
      -----------------------------
      IF SUBSTR(instr,ctr,1) NOT IN (' ','.','+','-','0','1','2'
                                    ,'3','4','5','6','7','8','9') THEN
         SET is_number = 'N';
         ITERATE wloop;
      END IF;
      IF SUBSTR(instr,ctr,1)  = ' ' AND
         bgn_blank            = 'N' THEN
         SET is_number = 'N';
         ITERATE wloop;
      END IF;
      IF SUBSTR(instr,ctr,1)  = '.' AND
         found_dot            = 'Y' THEN
         SET is_number = 'N';
         ITERATE wloop;
      END IF;
      IF SUBSTR(instr,ctr,1)  = '+'  AND
        (found_neg            = 'Y'  OR
         bgn_blank            = 'N') THEN
         SET is_number = 'N';
         ITERATE wloop;
      END IF;
      IF SUBSTR(instr,ctr,1)  = '-'  AND
        (found_neg            = 'Y'  OR
         bgn_blank            = 'N') THEN
         SET is_number = 'N';
         ITERATE wloop;
      END IF;
      -----------------------------
      --- MAINTAIN FLAGS & CTR  ---
      -----------------------------
      IF SUBSTR(instr,ctr,1) IN ('0','1','2','3','4'
                                ,'5','6','7','8','9') THEN
         SET found_num = 'Y';
      END IF;
      IF SUBSTR(instr,ctr,1)  = '.' THEN
         SET found_dot = 'Y';
      END IF;
      IF SUBSTR(instr,ctr,1)  = '+' THEN
         SET found_pos = 'Y';
      END IF;
      IF SUBSTR(instr,ctr,1)  = '-' THEN
         SET found_neg = 'Y';
      END IF;
Figure 1006, Check Numeric function, part 1 of 2






      IF SUBSTR(instr,ctr,1) <> ' ' THEN
         SET bgn_blank = 'N';
      END IF;
      SET ctr = ctr + 1;
   END WHILE wloop;
   IF found_num = 'N' THEN
      SET is_number = 'N';
   END IF;
   RETURN is_number;
END!   
       
WITH TEMP1 (C1) AS
(VALUES  '    123'
        ,'+123.45'
        ,'456    '
        ,' 10 2  '
        ,'   -.23'                                ANSWER
        ,'++12356'                                ====================
        ,'.012349'                                C1      C2 C3
        ,'    33%'                                ------- -- ---------
        ,'       '                                    123 Y  123.00000
        ,NULL)                                    +123.45 Y  123.45000
SELECT   C1                            AS C1      456     N          -
        ,isnumeric(C1)                 AS C2       10 2   N          -
        ,CASE                                        -.23 Y   -0.23000
            WHEN isnumeric(C1) = 'Y'              ++12356 N          -
            THEN DECIMAL(C1,10,6)                 .012349 Y    0.01234
            ELSE NULL                                 33% N          -
         END                           AS C3              N          -
FROM     TEMP1!                                   -       -          -
Figure 1007, Check Numeric function, part 2 of 2






SELECT   d_sal
        ,CHAR(d_sal)   AS d_chr
        ,DIGITS(d_sal) AS d_dgt
        ,i_sal
        ,CHAR(i_sal)   AS i_chr
        ,DIGITS(i_sal) AS i_dgt
FROM    (SELECT  DEC(salary - 11000,6,2)  AS d_sal
                ,SMALLINT(salary - 11000) AS i_sal
         FROM    staff
         WHERE   salary > 10000
           AND   salary < 12200
        )AS xxx                                                 ANSWER
ORDER BY d_sal;              =========================================
                             D_SAL   D_CHR    D_DGT  I_SAL I_CHR I_DGT
                             ------- -------- ------ ----- ----- -----
                             -494.10 -0494.10 049410  -494 -494  00494
                              -12.00 -0012.00 001200   -12 -12   00012
                              508.60 0508.60  050860   508 508   00508
                             1009.75 1009.75  100975  1009 1009  01009
Figure 1008, CHAR and DIGITS function usage






CREATE FUNCTION char_right(inval SMALLINT)
RETURNS CHAR(06)
RETURN  RIGHT(CHAR('',06) CONCAT RTRIM(CHAR(inval)),06);
       
       
CREATE FUNCTION char_right(inval INTEGER)
RETURNS CHAR(11)
RETURN  RIGHT(CHAR('',11) CONCAT RTRIM(CHAR(inval)),11);
       
       
CREATE FUNCTION char_right(inval BIGINT)
RETURNS CHAR(20)
RETURN  RIGHT(CHAR('',20) CONCAT RTRIM(CHAR(inval)),20);
Figure 1009, User-defined functions - convert integer to character






SELECT   i_sal                                             ANSWER
        ,char_right(i_sal) AS i_chr                        ===========
FROM    (SELECT  SMALLINT(salary - 11000) AS i_sal         I_SAL I_CHR
         FROM    staff                                     ----- -----
         WHERE   salary > 10000                             -494  -494
           AND   salary < 12200                              -12   -12
        )AS xxx                                              508   508
ORDER BY i_sal;                                             1009  1009
Figure 1010, Convert SMALLINT to CHAR






CREATE FUNCTION char_right(inval DECIMAL(20,2))
RETURNS CHAR(22)
RETURN  RIGHT(CHAR('',19)                               CONCAT
              REPLACE(SUBSTR(CHAR(inval*1),1,1),'0','') CONCAT
              STRIP(CHAR(ABS(BIGINT(inval))))           CONCAT
              '.'                                       CONCAT
              SUBSTR(DIGITS(inval),19,2),22);
Figure 1011, User-defined function - convert decimal to character






WITH                                                            ANSWER
temp1 (num, tst) AS                                  =================
   (VALUES  (1                                       NUM    TST  TCHAR
            ,DEC(0.01 ,20,2))                        --- ------ ------
    UNION ALL                                          1   0.01   0.01
    SELECT  num + 1                                    2  -0.03  -0.03
           ,tst * -3.21                                3   0.09   0.09
    FROM    temp1                                      4  -0.28  -0.28
    WHERE   num < 8)                                   5   0.89   0.89
select  num                                            6  -2.85  -2.85
       ,tst                                            7   9.14   9.14
       ,char_right(tst) AS tchar                       8 -29.33 -29.33
FROM    temp1;
Figure 1012, Convert DECIMAL to CHAR






CREATE FUNCTION comma_right(inval DECIMAL(20,2))
RETURNS CHAR(27)
LANGUAGE SQL
DETERMINISTIC
NO EXTERNAL ACTION
BEGIN ATOMIC
   DECLARE i INTEGER DEFAULT 17;
   DECLARE abs_inval BIGINT;
   DECLARE out_value CHAR(27);
   SET abs_inval = ABS(BIGINT(inval));
   SET out_value = RIGHT(CHAR('',19)  CONCAT
                         RTRIM(CHAR(BIGINT(inval))),19)
                   CONCAT '.'
                   CONCAT SUBSTR(DIGITS(inval),19,2);
   WHILE i > 2 DO
      IF SUBSTR(out_value,i-1,1) BETWEEN '0' AND '9' THEN
         SET out_value = SUBSTR(out_value,1,i-1) CONCAT
                         ','                     CONCAT
                         SUBSTR(out_value,i);
      ELSE
         SET out_value = ' ' CONCAT out_value;
      END IF;
      SET i = i - 3;
   END WHILE;
   RETURN out_value;
END    
Figure 1013, User-defined function - convert decimal to character - with commas






WITH                                                            ANSWER
temp1 (num) AS                    ====================================
  (VALUES (DEC(+1,20,2))          INPUT             OUTPUT
         ,(DEC(-1,20,2))          ----------------- ------------------
   UNION ALL                      -975460660753.97 -975,460,660,753.97
   SELECT  num * 987654.12              -987655.12         -987,655.12
   FROM    temp1                             -2.00               -2.00
   WHERE   ABS(num) < 1E10),                  0.00                0.00
temp2 (num) AS                           987653.12          987,653.12
  (SELECT  num - 1                 975460660751.97  975,460,660,751.97
   FROM    temp1)
SELECT   num              AS input
        ,comma_right(num) AS output
FROM     temp2
ORDER BY num;
Figure 1014, Convert DECIMAL to CHAR with commas






WITH tab1(ts1) AS
(VALUES CAST('1998-11-22-03.44.55.123456' AS TIMESTAMP))
       
SELECT                 ts1              =>  1998-11-22-03.44.55.123456
        ,          HEX(ts1)             =>  19981122034455123456
        ,      DEC(HEX(ts1),20)         =>  19981122034455123456.
        ,FLOAT(DEC(HEX(ts1),20))        =>  1.99811220344551e+019
        ,REAL (DEC(HEX(ts1),20))        =>  1.998112e+019
FROM     tab1;
Figure 1015, Convert Timestamp to number






SELECT   empno
        ,firstnme
        ,lastname
        ,job
FROM     employee
WHERE    empno < '000100'
ORDER BY empno;
Figure 1016, Sample query with no column control






SELECT   empno
        ,CASE :host-var-1
            WHEN 1 THEN firstnme
            ELSE        ''
         END         AS firstnme
        ,CASE :host-var-2
            WHEN 1 THEN lastname
            ELSE        ''
         END         AS lastname
        ,CASE :host-var-3
            WHEN 1 THEN VARCHAR(job)
            ELSE        ''
         END         AS job
FROM     employee
WHERE    empno < '000100'
ORDER BY empno;
Figure 1017, Sample query with column control






SELECT   id
        ,salary
        ,INT(salary / 1500)              AS len
        ,REPEAT('*',INT(salary / 1500))  AS salary_chart
FROM     staff
WHERE    id > 120                                               ANSWER
  AND    id < 190                  ===================================
ORDER BY id;                       ID   SALARY    LEN  SALARY_CHART
                                   ---  --------  ---  ---------------
                                   130  10505.90    7  *******
                                   140  21150.00   14  **************
                                   150  19456.50   12  ************
                                   160  22959.20   15  ***************
                                   170  12258.50    8  ********
                                   180  12009.75    8  ********
Figure 1018, Make chart using SQL






                                   ANSWER
                                   ===================================
                                   ID    SALARY   SALARY_CHART
WITH                               ---  --------  --------------------
temp1 (id, salary) AS              130  10505.90  *********
  (SELECT   id                     140  21150.00  ******************
           ,salary                 150  19456.50  ****************
   FROM     staff                  160  22959.20  ********************
   WHERE    id > 120               170  12258.50  **********
     AND    id < 190),             180  12009.75  **********
temp2 (max_sal) AS
  (SELECT   INT(MAX(salary)) / 20
   FROM     temp1)
SELECT   id
        ,salary
        ,VARCHAR(REPEAT('*',INT(salary / max_sal)),20) AS salary_chart
FROM     temp1
        ,temp2
ORDER BY id;
Figure 1019, Make chart of fixed length






SELECT   sex                                      ANSWER >>    SEX NUM
        ,COUNT(*) AS num                                       --- ---
FROM     stats                                                   F 595
GROUP BY sex                                                     M 405
ORDER BY sex;
Figure 1020, Use GROUP BY to get counts






WITH f (f) AS (SELECT COUNT(*) FROM stats WHERE sex = 'F')
    ,m (m) AS (SELECT COUNT(*) FROM stats WHERE sex = 'M')
SELECT  f, m
FROM    f, m;
Figure 1021, Use Common Table Expression to get counts






SELECT   SUM(CASE sex WHEN 'F' THEN 1 ELSE 0 END) AS female
        ,SUM(CASE sex WHEN 'M' THEN 1 ELSE 0 END) AS male
FROM     stats;
Figure 1022, Use CASE and SUM to get counts






SELECT   COUNT(*) AS total
        ,SUM(CASE sex WHEN 'F' THEN 1 ELSE 0 END) AS female
        ,SUM(CASE sex WHEN 'M' THEN 1 ELSE 0 END) AS male
FROM     stats;
Figure 1023, Use CASE and SUM to get counts






SELECT   years                                           ANSWER
        ,COUNT(*) AS #staff                              =============
FROM     staff                                           YEARS  #STAFF
WHERE    UCASE(name)  LIKE '%E%'                         -----  ------
  AND    years          <=  5                                1       1
GROUP BY years;                                              4       2
                                                             5       3
Figure 1024, Count staff joined per year






WITH list_years (year#) AS                                ANSWER
(VALUES (0),(1),(2),(3),(4),(5)                           ============
)                                                         YEARS #STAFF
SELECT   year#             AS years                       ----- ------
        ,COALESCE(#stff,0) AS #staff                          0      0
FROM     list_years                                           1      1
LEFT OUTER JOIN                                               2      0
        (SELECT   years                                       3      0
                  ,COUNT(*) AS #stff                          4      2
         FROM     staff                                       5      3
         WHERE    UCASE(name) LIKE '%E%'
           AND    years         <=  5
         GROUP BY years
        )AS xxx
ON       year# = years
ORDER BY 1;
Figure 1025, Count staff joined per year, all years






WITH list_years (year#) AS                                ANSWER
  (VALUES  SMALLINT(0)                                    ============
   UNION   ALL                                            YEARS #STAFF
   SELECT  year# + 1                                      ----- ------
   FROM    list_years                                         0      0
   WHERE   year# < 5)                                         1      1
SELECT   year#             AS years                           2      0
        ,COALESCE(#stff,0) AS #staff                          3      0
FROM     list_years                                           4      2
LEFT OUTER JOIN                                               5      3
        (SELECT   years
                  ,COUNT(*) AS #stff
         FROM     staff
         WHERE    UCASE(name) LIKE '%E%'
           AND    years         <=  5
         GROUP BY years
        )AS xxx
ON       year# = years
ORDER BY 1;
Figure 1026, Count staff joined per year, all years






WITH list_years (year#) AS                                      ANSWER
  (VALUES  SMALLINT(0)                                          ======
   UNION   ALL                                                   YEAR#
   SELECT  year# + 1                                             -----
   FROM    list_years                                                0
   WHERE   year# < 5)                                                2
SELECT   year#                                                       3
FROM     list_years y
WHERE    NOT EXISTS
        (SELECT *
         FROM   staff s
         WHERE  UCASE(s.name) LIKE '%E%'
           AND  s.years          =  y.year#)
ORDER BY 1;
Figure 1027, List years when no staff joined






WITH category (cat,subcat,dept) AS
(VALUES ('1ST','ROWS IN TABLE ','')
       ,('2ND','SALARY > $20K ','')
       ,('3RD','NAME LIKE ABC%','')
       ,('4TH','NUMBER MALES  ','')
 UNION 
 SELECT '5TH',deptname,deptno
 FROM   department
)      
SELECT   xxx.cat        AS "category"
        ,xxx.subcat     AS "subcategory/dept"
        ,SUM(xxx.found) AS "#rows"
FROM    (SELECT   cat.cat
                 ,cat.subcat
                 ,CASE
                     WHEN emp.empno IS NULL THEN 0
                     ELSE                        1
                  END AS found
         FROM     category cat
         LEFT OUTER JOIN
                  employee emp
         ON       cat.subcat      = 'ROWS IN TABLE'
         OR      (cat.subcat      = 'NUMBER MALES'
         AND      emp.sex         = 'M')
         OR      (cat.subcat      = 'SALARY > $20K'
         AND      emp.salary      >  20000)
         OR      (cat.subcat      = 'NAME LIKE ABC%'
         AND      emp.firstnme LIKE 'ABC%')
         OR      (cat.dept       <> ''
         AND      cat.dept        =  emp.workdept)
        )AS xxx
GROUP BY xxx.cat
        ,xxx.subcat
ORDER BY 1,2;
Figure 1028, Multiple counts in one pass, SQL






CATEGORY  SUBCATEGORY/DEPT               #ROWS
--------  -----------------------------  -----
1ST       ROWS IN TABLE                     32
2ND       SALARY > $20K                     25
3RD       NAME LIKE ABC%                     0
4TH       NUMBER MALES                      19
5TH       ADMINISTRATION SYSTEMS             6
5TH       DEVELOPMENT CENTER                 0
5TH       INFORMATION CENTER                 3
5TH       MANUFACTURING SYSTEMS              9
5TH       OPERATIONS                         5
5TH       PLANNING                           1
5TH       SOFTWARE SUPPORT                   4
5TH       SPIFFY COMPUTER SERVICE DIV.       3
5TH       SUPPORT SERVICES                   1
Figure 1029, Multiple counts in one pass, Answer






WITH   
temp1 (id, data) AS
   (VALUES (01,'SOME TEXT TO PARSE.')
          ,(02,'MORE SAMPLE TEXT.')
          ,(03,'ONE-WORD.')
          ,(04,'')
),     
temp2 (id, word#, word, data_left) AS
   (SELECT  id
           ,SMALLINT(1)
           ,SUBSTR(data,1,
            CASE LOCATE(' ',data)
               WHEN 0 THEN LENGTH(data)
               ELSE   LOCATE(' ',data)
            END)
           ,LTRIM(SUBSTR(data,
            CASE LOCATE(' ',data)
               WHEN 0 THEN LENGTH(data) + 1
               ELSE   LOCATE(' ',data)
            END))
    FROM    temp1
    WHERE   data <> ''
    UNION ALL
    SELECT  id
           ,word# + 1
           ,SUBSTR(data_left,1,
            CASE LOCATE(' ',data_left)
               WHEN 0 THEN LENGTH(data_left)
               ELSE   LOCATE(' ',data_left)
            END)
           ,LTRIM(SUBSTR(data_left,
            CASE LOCATE(' ',data_left)
               WHEN 0 THEN LENGTH(data_left) + 1
               ELSE   LOCATE(' ',data_left)
            END))
    FROM    temp2
    WHERE   data_left <> ''
)      
SELECT   *
FROM     temp2
ORDER BY 1,2;
Figure 1030, Break text into words - SQL






ID  WORD#  WORD       DATA_LEFT
--  -----  ---------  --------------
 1      1  SOME       TEXT TO PARSE.
 1      2  TEXT       TO PARSE.
 1      3  TO         PARSE.
 1      4  PARSE.
 2      1  MORE       SAMPLE TEXT.
 2      2  SAMPLE     TEXT.
 2      3  TEXT.
 3      1  ONE-WORD.
Figure 1031, Break text into words - Answer






WITH temp1 (dept,w#,name,all_names) AS
(SELECT   dept
         ,SMALLINT(1)
         ,MIN(name)
         ,VARCHAR(MIN(name),50)
 FROM     staff a
 GROUP BY dept
 UNION ALL
 SELECT   a.dept
         ,SMALLINT(b.w#+1)
         ,a.name
         ,b.all_names || ' ' || a.name
 FROM     staff a
         ,temp1 b
 WHERE    a.dept = b.dept
   AND    a.name > b.name
   AND    a.name =
         (SELECT MIN(c.name)
          FROM   staff c
          WHERE  c.dept = b.dept
            AND  c.name > b.name)
)      
SELECT   dept
        ,w#
        ,name AS max_name
        ,all_names
FROM     temp1 d
WHERE    w# =
        (SELECT MAX(w#)
         FROM   temp1 e
         WHERE  d.dept = e.dept)
ORDER BY dept;
Figure 1032, Denormalize Normalized Data - SQL






DEPT  W# MAX_NAME  ALL_NAMES
---- --  --------- -------------------------------------------
  10  4  Molinare  Daniels Jones Lu Molinare
  15  4  Rothman   Hanes Kermisch Ngan Rothman
  20  4  Sneider   James Pernal Sanders Sneider
  38  5  Quigley   Abrahams Marenghi Naughton O'Brien Quigley
  42  4  Yamaguchi Koonitz Plotz Scoutten Yamaguchi
  51  5  Williams  Fraye Lundquist Smith Wheeler Williams
  66  5  Wilson    Burke Gonzales Graham Lea Wilson
  84  4  Quill     Davis Edwards Gafney Quill
Figure 1033, Denormalize Normalized Data - Answer






CREATE FUNCTION list_names(indept SMALLINT)               IMPORTANT
RETURNS VARCHAR(50)                                       ============
BEGIN ATOMIC                                              This example
   DECLARE outstr VARCHAR(50) DEFAULT '';                 uses an "!"
   FOR list_names AS                                      as the stmt
      SELECT   name                                       delimiter.
      FROM     staff
      WHERE    dept = indept
      ORDER BY name
   DO  
      SET outstr = outstr || name || ' ';
   END FOR;
   SET outstr = rtrim(outstr);
   RETURN outstr;
END!   
       
SELECT   dept              AS DEPT
        ,SMALLINT(cnt)     AS W#
        ,mxx               AS MAX_NAME
        ,list_names(dept)  AS ALL_NAMES
FROM    (SELECT   dept
                 ,COUNT(*)  as cnt
                 ,MAX(name) AS mxx
         FROM     staff
         GROUP BY dept
        )as ddd
ORDER BY dept!
Figure 1034, Creating a function to denormalize names






CREATE FUNCTION num_to_char(inval SMALLINT)
RETURNS CHAR(06)
RETURN RIGHT(CHAR('',06) CONCAT RTRIM(CHAR(inval)),06);
       
CREATE FUNCTION num_to_char(inval DECIMAL(9,2))
RETURNS CHAR(10)
RETURN RIGHT(CHAR('',7) CONCAT RTRIM(CHAR(BIGINT(inval))),7)
       CONCAT '.'
       CONCAT SUBSTR(DIGITS(inval),8,2);
       
CREATE FUNCTION right_justify(inval CHAR(5))
RETURNS CHAR(10)
RETURN  RIGHT(CHAR('',10) || RTRIM(inval),10);
Figure 1035, Data Transformation Functions






WITH   
data_input AS
  (SELECT   dept
           ,job
           ,SUM(salary) AS sum_sal
   FROM     staff
   WHERE    id       < 200
     AND    name    <> 'Sue'
     AND    salary   > 10000
   GROUP BY dept
           ,job),
jobs_list AS
  (SELECT   job
           ,ROW_NUMBER() OVER(ORDER BY job ASC)  AS job#A
           ,ROW_NUMBER() OVER(ORDER BY job DESC) AS job#D
   FROM     data_input
   GROUP BY job),
dept_list AS
  (SELECT   dept
   FROM     data_input
   GROUP BY dept),
dept_jobs_list AS
  (SELECT   dpt.dept
           ,job.job
           ,job.job#A
           ,job.job#D
   FROM     jobs_list job
   FULL OUTER JOIN
            dept_list dpt
   ON       1 = 1),
data_all_jobs AS
  (SELECT   djb.dept
           ,djb.job
           ,djb.job#A
           ,djb.job#D
           ,COALESCE(dat.sum_sal,0) AS sum_sal
   FROM     dept_jobs_list djb
   LEFT OUTER JOIN
            data_input     dat
   ON       djb.dept = dat.dept
   AND      djb.job  = dat.job),
data_transform (dept, job#A, job#D, outvalue) AS
  (SELECT   dept
           ,job#A
           ,job#D
           ,VARCHAR(num_to_char(sum_sal),250)
   FROM     data_all_jobs
   WHERE    job#A = 1
   UNION ALL
   SELECT   dat.dept
           ,dat.job#A
           ,dat.job#D
           ,trn.outvalue || ',' || num_to_char(dat.sum_sal)
   FROM     data_transform trn
           ,data_all_jobs  dat
   WHERE    trn.dept  = dat.dept
     AND    trn.job#A = dat.job#A - 1),
data_last_row AS
  (SELECT   dept
           ,num_to_char(dept) AS dept_char
           ,outvalue
   FROM     data_transform
   WHERE    job#D = 1),
Figure 1036, Transform numeric data - part 1 of 2






jobs_transform (job#A, job#D, outvalue) AS
  (SELECT   job#A
           ,job#D
           ,VARCHAR(right_justify(job),250)
   FROM     jobs_list
   WHERE    job#A = 1
   UNION ALL
   SELECT   job.job#A
           ,job.job#D
           ,trn.outvalue || ',' || right_justify(job.job)
   FROM     jobs_transform trn
           ,jobs_list      job
   WHERE    trn.job#A = job.job#A - 1),
jobs_last_row AS
  (SELECT   0        AS dept
           ,'  DEPT' AS dept_char
           ,outvalue
   FROM     jobs_transform
   WHERE    job#D = 1),
data_and_jobs AS
  (SELECT   dept
           ,dept_char
           ,outvalue
   FROM     jobs_last_row
   UNION ALL
   SELECT   dept
           ,dept_char
           ,outvalue
   FROM     data_last_row)
SELECT   dept_char || ',' ||
         outvalue  AS output
FROM     data_and_jobs
ORDER BY dept;
Figure 1037, Transform numeric data - part 2 of 2






DATA_INPUT                       OUTPUT
===================              =====================================
DEPT JOB   SUM_SAL               DEPT,     Clerk,       Mgr,     Sales
---- ----- --------                10,      0.00,  22959.20,      0.00
  10 Mgr   22959.20                15,  24766.70,  20659.80,  16502.83
  15 Clerk 24766.70                20,  27757.35,  18357.50,  78171.25
  15 Mgr   20659.80                38,  24964.50,  77506.75,  34814.30
  15 Sales 16502.83                42,  10505.90,  18352.80,  18001.75
  20 Clerk 27757.35                51,      0.00,  21150.00,  19456.50
  20 Mgr   18357.50
  20 Sales 78171.25
  38 Clerk 24964.50
  38 Mgr   77506.75
  38 Sales 34814.30
  42 Clerk 10505.90
  42 Mgr   18352.80
  42 Sales 18001.75
  51 Mgr   21150.00
  51 Sales 19456.50
Figure 1038, Contents of first temporary table and final output






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE FUNCTION reverse(instr VARCHAR(50))                This example
RETURNS VARCHAR(50)                                       uses an "!"
BEGIN ATOMIC                                              as the stmt
   DECLARE outstr  VARCHAR(50) DEFAULT '';                delimiter.
   DECLARE curbyte SMALLINT    DEFAULT 0;
   SET curbyte = LENGTH(RTRIM(instr));
   WHILE curbyte >= 1 DO
      SET outstr  = outstr || SUBSTR(instr,curbyte,1);
      SET curbyte = curbyte - 1;
   END WHILE;
   RETURN outstr;
END!   
                                                  ANSWER
SELECT   id             AS ID                     ====================
        ,name           AS NAME1                  ID NAME1    NAME2
        ,reverse(name)  AS NAME2                  -- -------- -------
FROM     staff                                    10 Sanders  srednaS
WHERE    id < 40                                  20 Pernal   lanreP
ORDER BY id!                                      30 Marenghi ihgneraM
Figure 1039, Reversing character field






SELECT   id                             AS ID
        ,salary                         AS SALARY1
        ,DEC(reverse(CHAR(salary)),7,4) AS SALARY2
FROM     staff                                                  ANSWER
WHERE    id < 40                                   ===================
ORDER BY id;                                       ID SALARY1  SALARY2
                                                   -- -------- -------
                                                   10 18357.50  5.7538
                                                   20 78171.25 52.1718
                                                   30 77506.75 57.6057
Figure 1040, Reversing numeric field






--#SET DELIMITER !                                        IMPORTANT
                                                          ============
CREATE FUNCTION Fibonacci (inval1 INTEGER                 This example
                          ,inval2 INTEGER                 uses an "!"
                          ,loopno INTEGER)                as the stmt
RETURNS VARCHAR(500)                                      delimiter.
BEGIN ATOMIC
   DECLARE loopctr  INTEGER DEFAULT 0;
   DECLARE tempval1 BIGINT;
   DECLARE tempval2 BIGINT;
   DECLARE tempval3 BIGINT;
   DECLARE outvalue  VARCHAR(500);
   SET tempval1 = inval1;
   SET tempval2 = inval2;
   SET outvalue = RTRIM(LTRIM(CHAR(tempval1))) || ', ' ||
                  RTRIM(LTRIM(CHAR(tempval2)));
   calc: WHILE loopctr < loopno DO
      SET tempval3 = tempval1 + tempval2;
      SET tempval1 = tempval2;
      SET tempval2 = tempval3;
      SET outvalue = outvalue || ', ' || RTRIM(LTRIM(CHAR(tempval3)));
      SET loopctr  = loopctr + 1;
      IF LENGTH(outvalue) > 480 THEN
         SET outvalue = outvalue || ' etc...';
         LEAVE calc;
      END IF;
   END WHILE;
   RETURN outvalue;
END!   
Figure 1041, Fibonacci Series function






WITH temp1 (v1,v2,lp) AS
  (VALUES (00,01,11)
         ,(12,61,10)
         ,(02,05,09)
         ,(01,-1,08))
SELECT   t1.*
        ,Fibonacci(v1,v2,lp) AS sequence
FROM     temp1 t1;
                                                               ANSWER
=====================================================================
V1 V2 LP  SEQUENCE
-- -- --  -----------------------------------------------------------
 0  1 11  0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144
12 61 10  12, 61, 73, 134, 207, 341, 548, 889, 1437, 2326, 3763, 6089
 2  5  9  2, 5, 7, 12, 19, 31, 50, 81, 131, 212, 343
 1 -1  8  1, -1, 0, -1, -1, -2, -3, -5, -8, -13
Figure 1042, Fibonacci Series generation






CREATE FUNCTION business_days (lo_date DATE, hi_date DATE)
RETURNS INTEGER
BEGIN ATOMIC
   DECLARE bus_days INTEGER DEFAULT 0;
   DECLARE cur_date DATE;
   SET     cur_date = lo_date;
   WHILE cur_date < hi_date DO
      IF DAYOFWEEK(cur_date) IN (2,3,4,5,6) THEN
         SET bus_days = bus_days + 1;                     IMPORTANT
      END IF;                                             ============
      SET cur_date = cur_date + 1 DAY;                    This example
   END WHILE;                                             uses an "!"
   RETURN bus_days;                                       as the stmt
END!                                                      delimiter.
Figure 1043, Calculate number of business days between two dates






WITH temp1 (ld, hd) AS
  (VALUES (DATE('2006-01-10'),DATE('2007-01-01'))
         ,(DATE('2007-01-01'),DATE('2007-01-01'))
         ,(DATE('2007-02-10'),DATE('2007-01-01')))
SELECT   t1.*
        ,DAYS(hd) - DAYS(ld)  AS diff
        ,business_days(ld,hd) AS bdays
FROM     temp1 t1;                                              ANSWER
                                      ================================
                                      LD          HD        DIFF BDAYS
                                      ---------- ---------- ---- -----
                                      2006-01-10 2007-01-01  356   254
                                      2007-01-01 2007-01-01    0     0
                                      2007-02-10 2007-01-01  -40     0
Figure 1044, Use business-day function






WITH temp1 (num,ts1,ts2) AS
(VALUES (INT(1)
        ,TIMESTAMP(GENERATE_UNIQUE())
        ,TIMESTAMP(GENERATE_UNIQUE()))
 UNION ALL
 SELECT  num + 1
        ,ts1
        ,TIMESTAMP(GENERATE_UNIQUE())
 FROM    temp1
 WHERE   TIMESTAMPDIFF(2,CHAR(ts2-ts1)) < 4
)      
SELECT  MAX(num)  AS #loops
       ,MIN(ts2)  AS bgn_timestamp
       ,MAX(ts2)  AS end_timestamp
FROM    temp1;
                                                                ANSWER
          ============================================================
          #LOOPS BGN_TIMESTAMP              END_TIMESTAMP
          ------ -------------------------- --------------------------
           58327 2001-08-09-22.58.12.754579 2001-08-09-22.58.16.754634
Figure 1045, Run query for four seconds






CREATE FUNCTION pause(inval INT)
RETURNS INTEGER
NOT DETERMINISTIC
EXTERNAL ACTION
RETURN 
WITH ttt (num, strt, stop) AS
  (VALUES (1
          ,TIMESTAMP(GENERATE_UNIQUE())
          ,TIMESTAMP(GENERATE_UNIQUE()))
   UNION ALL
   SELECT  num + 1
          ,strt
          ,TIMESTAMP(GENERATE_UNIQUE())
   FROM    ttt
   WHERE   TIMESTAMPDIFF(2,CHAR(stop - strt)) < inval
  )    
SELECT  MAX(num)
FROM    ttt;
Figure 1046, Function that pauses for "n" seconds






SELECT   id
        ,SUBSTR(CHAR(TIMESTAMP(GENERATE_UNIQUE())),18) AS ss_mmmmmm
        ,pause(id / 10)                                AS #loops
        ,SUBSTR(CHAR(TIMESTAMP(GENERATE_UNIQUE())),18) AS ss_mmmmmm
FROM     staff
WHERE    id < 31;
                                                                ANSWER
                                         =============================
                                         ID SS_MMMMMM #LOOPS SS_MMMMMM
                                         -- --------- ------ ---------
                                         10 50.068593  76386 50.068587
                                         20 52.068744 144089 52.068737
                                         30 55.068930 206101 55.068923
Figure 1047, Query that uses pause function






--#SET DELIMITER !
       
CREATE FUNCTION sort_char(in_val VARCHAR(20),sort_dir VARCHAR(1))
RETURNS VARCHAR(20)
BEGIN ATOMIC
   DECLARE cur_pos SMALLINT;
   DECLARE do_sort CHAR(1);
   DECLARE out_val VARCHAR(20);
   IF UCASE(sort_dir) NOT IN ('A','D') THEN
      SIGNAL SQLSTATE '75001'
      SET MESSAGE_TEXT = 'Sort order not ''A'' or ''D''';
   END IF;
   SET out_val = in_val;
   SET do_sort = 'Y';
   WHILE do_sort = 'Y' DO
       SET do_sort = 'N';                                 IMPORTANT
       SET cur_pos =  1;                                  ============
       WHILE cur_pos < length(in_val) DO                  This example
          IF  (UCASE(sort_dir)             = 'A'          uses an "!"
          AND  SUBSTR(out_val,cur_pos+1,1) <              as the stmt
                SUBSTR(out_val,cur_pos,1))                delimiter.
          OR  (UCASE(sort_dir)             = 'D'
          AND  SUBSTR(out_val,cur_pos+1,1) >
               SUBSTR(out_val,cur_pos,1)) THEN
             SET do_sort = 'Y';
             SET out_val = CASE
                              WHEN cur_pos = 1
                              THEN ''
                              ELSE SUBSTR(out_val,1,cur_pos-1)
                           END
                           CONCAT SUBSTR(out_val,cur_pos+1,1)
                           CONCAT SUBSTR(out_val,cur_pos  ,1)
                           CONCAT
                           CASE
                              WHEN cur_pos = length(in_val) - 1
                              THEN ''
                              ELSE SUBSTR(out_val,cur_pos+2)
                           END;
          END IF;
          SET cur_pos = cur_pos + 1;
       END WHILE;
   END WHILE;
   RETURN out_val;
END!   
Figure 1048, Define sort-char function






WITH word1 (w#, word_val) AS             ANSWER
   (VALUES(1,'12345678')                 =============================
         ,(2,'ABCDEFG')                  W# WORD_VAL SA       SD
         ,(3,'AaBbCc')                   -- --------- ------- --------
         ,(4,'abccb')                     1 12345678 12345678 87654321
         ,(5,'''%#.')                     2 ABCDEFG  ABCDEFG  GFEDCBA
         ,(6,'bB')                        3 AaBbCc   aAbBcC   CcBbAa
         ,(7,'a')                         4 abccb    abbcc    ccbba
         ,(8,''))                         5 '%#.     .'#%     %#'.
SELECT   w#                               6 bB       bB       Bb
        ,word_val                         7 a        a        a
        ,sort_char(word_val,'a') sa       8
        ,sort_char(word_val,'D') sd
FROM     word1
ORDER BY w#;
Figure 1049, Use sort-char function






WITH numbered_rows AS
  (SELECT   s.*
           ,ROW_NUMBER() OVER(PARTITION BY job
                              ORDER     BY salary, id) AS row#
   FROM     staff s
   WHERE    comm    > 0
     AND    name LIKE '%e%'),
median_row_num AS
  (SELECT   job
           ,(MAX(row# + 1.0) / 2) - 0.5 AS med_lo
           ,(MAX(row# + 1.0) / 2) + 0.5 AS med_hi
   FROM     numbered_rows
   GROUP BY job)
SELECT   nn.job
        ,DEC(AVG(nn.salary),7,2) AS med_sal
FROM     numbered_rows   nn                             ANSWER
        ,median_row_num  mr                             ==============
WHERE    nn.job        = mr.job                         JOB   MED_SAL
  AND    nn.row# BETWEEN mr.med_lo AND mr.med_hi        ----- --------
GROUP BY nn.job                                         Clerk 13030.50
ORDER BY nn.job;                                        Sales 17432.10
Figure 1050, Calculating the median






I
WITH numbered_rows AS
  (SELECT   s.*
           ,ROW_NUMBER() OVER(PARTITION BY job
                              ORDER     BY salary, id) AS row#
   FROM     staff s
   WHERE    comm    > 0
     AND    name LIKE '%e%'),
median_row_num AS
  (SELECT   job
           ,(MAX(row# + 1.0) / 2) - 0.5 AS med_lo
           ,(MAX(row# + 1.0) / 2) + 0.5 AS med_hi
           ,DEC(AVG(salary),7,2)        AS avg_sal
           ,COUNT(*)                    AS #rows
   FROM     numbered_rows
   GROUP BY job)
SELECT   nn.job
        ,DEC(AVG(nn.salary),7,2) AS med_sal
        ,MAX(mr.avg_sal)         AS avg_sal
        ,MAX(mr.#rows)           AS #r
FROM     numbered_rows   nn
        ,median_row_num  mr                 ANSWER
WHERE    nn.job        = mr.job             ==========================
  AND    nn.row# BETWEEN mr.med_lo          JOB   MED_SAL  AVG_SAL  #R
                     AND mr.med_hi          ----- -------- -------- --
GROUP BY nn.job                             Clerk 13030.50 12857.56  7
ORDER BY nn.job;                            Sales 17432.10 17460.93  4
Figure 1051, Get median plus average






WITH numbered_rows AS
  (SELECT   s.*
           ,ROW_NUMBER() OVER(PARTITION BY job
                              ORDER     BY salary, id) AS row#
   FROM     staff s
   WHERE    comm    > 0
     AND    name LIKE '%e%'),
median_row_num AS
  (SELECT   job
           ,MAX(row# + 1) / 2 AS med_row#
   FROM     numbered_rows
   GROUP BY job)
SELECT   nn.job
        ,nn.salary AS med_sal                           ANSWER
FROM     numbered_rows   nn                             ==============
        ,median_row_num  mr                             JOB   MED_SAL
WHERE    nn.job  = mr.job                               ----- --------
  AND    nn.row# = mr.med_row#                          Clerk 13030.50
ORDER BY nn.job;                                        Sales 16858.20
Figure 1052, Calculating the median






WITH numbered_rows AS
  (SELECT   s.*
           ,ROW_NUMBER() OVER(PARTITION BY job
                              ORDER     BY salary, id) AS row#
   FROM     staff s
   WHERE    comm    > 0
     AND    name LIKE '%e%')
SELECT   job
        ,salary AS med_sal
FROM     numbered_rows
WHERE   (job,row#) IN                                   ANSWER
        (SELECT   job                                   ==============
                 ,MAX(row# + 1) / 2                     JOB   MED_SAL
         FROM     numbered_rows                         ----- --------
         GROUP BY job)                                  Clerk 13030.50
ORDER BY job;                                           Sales 16858.20
Figure 1053, Calculating the median






WITH numbered_rows AS
  (SELECT   s.*
           ,ROW_NUMBER() OVER(PARTITION BY job
                              ORDER     BY salary, id) AS row#
   FROM     staff s
   WHERE    comm    > 0
     AND    name LIKE '%e%')
SELECT   r1.*
        ,(SELECT  r2.salary
          FROM    numbered_rows r2
          WHERE   r2.job  = r1.job
            AND   r2.row# = (SELECT  MAX(r3.row# + 1) / 2
                             FROM    numbered_rows r3
                             WHERE   r2.job = r3.job)) AS med_sal
FROM     numbered_rows r1
ORDER BY job
        ,salary;
Figure 1054, List matching rows and median






CREATE FUNCTION hex_to_int(input_val VARCHAR(16))
RETURNS BIGINT
BEGIN ATOMIC
   DECLARE parse_val VARCHAR(16) DEFAULT '';
   DECLARE sign_val  BIGINT      DEFAULT 1;
   DECLARE out_val   BIGINT      DEFAULT 0;
   DECLARE cur_exp   BIGINT      DEFAULT 1;
   DECLARE input_len SMALLINT    DEFAULT 0;
   DECLARE cur_byte  SMALLINT    DEFAULT 1;
   IF LENGTH(input_val) NOT IN (4,8,16) THEN
      SIGNAL SQLSTATE VALUE '70001' SET MESSAGE_TEXT = 'Length wrong';
   END IF;
   SET input_len = LENGTH(input_val);
   WHILE cur_byte <= input_len DO
      SET parse_val = parse_val                         ||
                      SUBSTR(input_val,cur_byte + 1,1)  ||
                      SUBSTR(input_val,cur_byte + 0,1);
      SET cur_byte  = cur_byte + 2;
   END WHILE;
   IF SUBSTR(parse_val,input_len,1) BETWEEN '8' AND 'F' THEN
      SET sign_val  = -1;
      SET out_val   = -1;
      SET parse_val = TRANSLATE(parse_val
                               ,'0123456789ABCDEF'
                               ,'FEDCBA9876543210');
   END IF;
   SET cur_byte = 1;
   WHILE cur_byte <= input_len DO
      SET out_val = out_val  +
                   (cur_exp  *
                    sign_val *
                    CASE SUBSTR(parse_val,cur_byte,1)
                       WHEN '0' THEN 00    WHEN '1' THEN 01
                       WHEN '2' THEN 02    WHEN '3' THEN 03
                       WHEN '4' THEN 04    WHEN '5' THEN 05
                       WHEN '6' THEN 06    WHEN '7' THEN 07
                       WHEN '8' THEN 08    WHEN '9' THEN 09
                       WHEN 'A' THEN 10    WHEN 'B' THEN 11
                       WHEN 'C' THEN 12    WHEN 'D' THEN 13
                       WHEN 'E' THEN 14    WHEN 'F' THEN 15
                    END);
      IF cur_byte < input_len THEN
         SET cur_exp  = cur_exp * 16;
      END IF;
      SET cur_byte = cur_byte + 1;
   END WHILE;
   RETURN out_val;
END    
Figure 1055, Trigger to convert HEX value to integer






WITH temp1 (num) AS                                             ANSWER
(VALUES (SMALLINT(+0))                              ==================
       ,(SMALLINT(+1))                              NUM    HEX  H2I
       ,(SMALLINT(-1))                              ------ ---- ------
       ,(SMALLINT(+32767))                               0 0000      0
       ,(SMALLINT(-32768)))                              1 0100      1
SELECT   num                                            -1 FFFF     -1
        ,HEX(num)             AS hex                 32767 FF7F  32767
        ,hex_to_int(HEX(num)) AS h2i                -32768 0080 -32768
FROM     temp1;
Figure 1056, Using trigger to convert data






WITH                                                            ANSWER
temp1 (num) AS                            ============================
  (VALUES (INTEGER(0))                    NUM       HEX      H2I
   UNION ALL                              --------- -------- ---------
   SELECT  (num + 1) * 7                  -87432800 A0E1C9FA -87432800
   FROM    temp1                          -12490387 6D6941FF -12490387
   WHERE   num < 1E6),                     -1784328 F8C5E4FF  -1784328
temp2 (sgn) AS                              -254891 551CFCFF   -254891
  (VALUES (+1)                               -36400 D071FFFF    -36400
         ,(-13)),                             -5187 BDEBFFFF     -5187
temp3 (num) AS                                 -728 28FDFFFF      -728
  (SELECT  DISTINCT                             -91 A5FFFFFF       -91
           num * sgn                              0 00000000         0
   FROM    temp1                                  7 07000000         7
          ,temp2)                                56 38000000        56
SELECT   num                                    399 8F010000       399
        ,HEX(num)             AS hex           2800 F00A0000      2800
        ,hex_to_int(HEX(num)) AS h2i          19607 974C0000     19607
FROM     temp3                               137256 28180200    137256
ORDER BY num;                                960799 1FA90E00    960799
                                            6725600 E09F6600   6725600
Figure 1057, Using trigger to convert data






WITH temp1 (c1,t1,t2) AS (VALUES                             ANSWER
   ('A'                                                      =========
   ,TIMESTAMP('1996-05-01-24.00.00.000000')                  
   ,TIMESTAMP('1996-05-02-00.00.00.000000') ))
SELECT c1
FROM   temp1
WHERE  t1 = t2;
Figure 1058, Timestamp comparison - Incorrect






WITH temp1 (c1,t1,t2) AS (VALUES                                ANSWER
   ('A'                                                         ======
   ,TIMESTAMP('1996-05-01-24.00.00.000000')                         C1
   ,TIMESTAMP('1996-05-02-00.00.00.000000') ))                      --
SELECT c1                                                           A
FROM   temp1
WHERE  t1 + 0 MICROSECOND = t2 + 0 MICROSECOND;
Figure 1059, Timestamp comparison - Correct






CREATE TABLE supermarket_sales
(sales_ts   TIMESTAMP      NOT NULL
,sales_val  DECIMAL(8,2)   NOT NULL
,PRIMARY KEY(sales_ts));
Figure 1060, Sample Table






INSERT INTO supermarket_sales VALUES
('2003-08-01-24.00.00.000000',123.45);
Figure 1061, Insert row






SELECT   *
FROM     supermarket_sales
WHERE    DATE(sales_ts) = '2003-08-01'
ORDER BY sales_ts;
Figure 1062, Select rows for given date






SELECT   *
FROM     supermarket_sales
WHERE    sales_ts BETWEEN '2003-08-01-00.00.00'
                      AND '2003-08-01-24.00.00'
ORDER BY sales_ts;
Figure 1063, Select rows for given date






SELECT   creator                                               ANSWER
FROM     sysibm.systables                                     ========
WHERE    creator = 'ZZZ';                                     
Figure 1064, Query with no matching rows (1 of 8)






SELECT   MAX(creator)                                           ANSWER
FROM     sysibm.systables                                       ======
WHERE    creator = 'ZZZ';                                       
Figure 1065, Query with no matching rows (2 of 8)






SELECT   MAX(creator)                                          ANSWER
FROM     sysibm.systables                                     ========
WHERE    creator = 'ZZZ'                                      
HAVING   MAX(creator) IS NOT NULL;
Figure 1066, Query with no matching rows (3 of 8)






SELECT   MAX(creator)                                          ANSWER
FROM     sysibm.systables                                     ========
WHERE    creator      = 'ZZZ'                                 
HAVING   MAX(creator) = 'ZZZ';
Figure 1067, Query with no matching rows (4 of 8)






SELECT   MAX(creator)                                          ANSWER
FROM     sysibm.systables                                     ========
WHERE    creator = 'ZZZ'                                      
GROUP BY creator;
Figure 1068, Query with no matching rows (5 of 8)






SELECT   creator                                               ANSWER
FROM     sysibm.systables                                     ========
WHERE    creator = 'ZZZ'                                      
GROUP BY creator;
Figure 1069, Query with no matching rows (6 of 8)






SELECT   COUNT(*)                                              ANSWER
FROM     sysibm.systables                                     ========
WHERE    creator = 'ZZZ'                                      
GROUP BY creator;
Figure 1070, Query with no matching rows (7 of 8)






SELECT   COUNT(*)                                               ANSWER
FROM     sysibm.systables                                       ======
WHERE    creator = 'ZZZ';                                            0
Figure 1071, Query with no matching rows (8 of 8)






SELECT   COALESCE(name,noname)  AS nme                    ANSWER
        ,COALESCE(salary,nosal) AS sal                    ============
FROM    (SELECT   'NO NAME' AS noname                     NME     SAL
                 ,0         AS nosal                      ------- ----
         FROM     sysibm.sysdummy1                        NO NAME 0.00
        )AS nnn
LEFT OUTER JOIN
        (SELECT   *
         FROM     staff
         WHERE    id  < 5
        )AS xxx
ON       1 = 1
ORDER BY name;
Figure 1072, Always get a row, example 1 of 2






WITH nnn (noname, nosal) AS                               ANSWER
(VALUES ('NO NAME',0))                                    ============
SELECT   COALESCE(name,noname)  AS nme                    NME     SAL
        ,COALESCE(salary,nosal) AS sal                    ------- ----
FROM     nnn                                              NO NAME 0.00
LEFT OUTER JOIN
        (SELECT   *
         FROM     staff
         WHERE    id  < 5
        )AS xxx
ON       1 = 1
ORDER BY NAME;
Figure 1073, Always get a row, example 2 of 2






SELECT   DATE('2001-09-22')                                   ANSWER
FROM     sysibm.sysdummy1;                                  ==========
                                                            2001-09-22
Figure 1074, Convert value to DB2 date, right






SELECT   DATE(2001-09-22)                                     ANSWER
FROM     sysibm.sysdummy1;                                  ==========
                                                            0006-05-24
Figure 1075, Convert value to DB2 date, wrong






SELECT   id                                                ANSWER
        ,name                                              ===========
FROM     staff                                             ID NAME
WHERE    id  <= 100                                        -- --------
  AND    id   = (INT(RAND()* 10) * 10) + 10                30 Marenghi
ORDER BY id;                                               60 Quigley
Figure 1076, Get random rows - Incorrect






WITH temp AS                                      ANSWER
(SELECT   id                                      ====================
         ,name                                    ID  NAME     RAN EQL
         ,(INT(RAND(0)* 10) * 10) + 10 AS ran     --- -------- --- ---
 FROM     staff                                    10 Sanders   10 Y
 WHERE    id <= 100                                20 Pernal    30
)                                                  30 Marenghi  70
SELECT   t.*                                       40 O'Brien   10
        ,CASE id                                   50 Hanes     30
            WHEN ran THEN 'Y'                      60 Quigley   40
            ELSE          ' '                      70 Rothman   30
         END AS eql                                80 James    100
FROM     temp t                                    90 Koonitz   40
ORDER BY id;                                      100 Plotz    100 Y
Figure 1077, Get random rows - Explanation






WITH                                                       ANSWER
staff_numbered AS                                          ===========
  (SELECT  s.*                                             ID  NAME
          ,ROW_NUMBER() OVER() AS row#                     --- -------
   FROM    staff s                                          10 Sanders
   WHERE   id <= 100                                        20 Pernal
),                                                          90 Koonitz
count_rows AS
  (SELECT  MAX(row#) AS #rows
   FROM    staff_numbered
),     
random_values (RAN#) AS
  (VALUES (RAND())
         ,(RAND())
         ,(RAND())
),     
rows_t0_get AS
  (SELECT INT(ran# * #rows) + 1 AS get_row
   FROM   random_values
         ,count_rows
)      
SELECT   id
        ,name
FROM     staff_numbered
        ,rows_t0_get
WHERE    row# = get_row
ORDER BY id;
Figure 1078, Get random rows - Non-distinct






SELECT   id                                                 ANSWER
        ,name                                               ==========
FROM    (SELECT   s2.*                                      ID NAME
                 ,ROW_NUMBER() OVER(ORDER BY r1) AS r2      -- -------
         FROM    (SELECT   s1.*                             10 Sanders
                          ,RAND() AS r1                     40 O'Brien
                  FROM     staff s1                         60 Quigley
                  WHERE    id <= 100
                 )AS s2
        )as s3
WHERE    r2 <= 3
ORDER BY id;
Figure 1079, Get random rows - Distinct






WITH temp1 (bgn_tstamp, elp_sec) AS
(VALUES (TIMESTAMP('2001-01-15-01.02.03.000000'), 1.234)
       ,(TIMESTAMP('2001-01-15-01.02.03.123456'), 1.234)
)      
SELECT   bgn_tstamp
        ,elp_sec
        ,bgn_tstamp + elp_sec SECONDS AS end_tstamp
FROM     temp1;
       
       
       
       ANSWER
       ======
       BGN_TSTAMP                  ELP_SEC  END_TSTAMP
       --------------------------  -------  --------------------------
       2001-01-15-01.02.03.000000    1.234  2001-01-15-01.02.04.000000
       2001-01-15-01.02.03.123456    1.234  2001-01-15-01.02.04.123456
Figure 1080, Date/Time manipulation - wrong






WITH temp1 (bgn_tstamp, elp_sec) AS
(VALUES (TIMESTAMP('2001-01-15-01.02.03.000000'), 1.234)
       ,(TIMESTAMP('2001-01-15-01.02.03.123456'), 1.234)
)      
SELECT   bgn_tstamp
        ,elp_sec
        ,bgn_tstamp + (elp_sec *1E6) MICROSECONDS AS end_tstamp
FROM     temp1;
       
       
       
       ANSWER
       ======
       BGN_TSTAMP                  ELP_SEC  END_TSTAMP
       --------------------------  -------  --------------------------
       2001-01-15-01.02.03.000000    1.234  2001-01-15-01.02.04.234000
       2001-01-15-01.02.03.123456    1.234  2001-01-15-01.02.04.357456
Figure 1081, Date/Time manipulation - right






WITH temp1 (c0,c1,v1) AS (VALUES                                ANSWER
   ('A',CHAR(' ',1),VARCHAR(' ',1)),                            ======
   ('B',CHAR(' ',1),VARCHAR('' ,1)))                                C0
SELECT c0                                                           --
FROM   temp1                                                         A
WHERE  c1 = v1                                                       B
  AND  c1 LIKE ' ';
Figure 1082, Use LIKE on CHAR field






WITH temp1 (c0,c1,v1) AS (VALUES                                ANSWER
   ('A',CHAR(' ',1),VARCHAR(' ',1)),                            ======
   ('B',CHAR(' ',1),VARCHAR('' ,1)))                                C0
SELECT c0                                                           --
FROM   temp1                                                         A
WHERE  c1 = v1
  AND  v1 LIKE ' ';
Figure 1083, Use LIKE on VARCHAR field






WITH temp1 (c0,c1,v1) AS (VALUES                                ANSWER
   ('A',CHAR(' ',1),VARCHAR(' ',1)),                            ======
   ('B',CHAR(' ',1),VARCHAR('' ,1)))                                C0
SELECT c0                                                           --
FROM   temp1                                                         A
WHERE  c1 = v1                                                       B
  AND  RTRIM(v1) LIKE '';
Figure 1084, Use RTRIM to remove trailing blanks






WITH temp1 (yymmdd) AS                      ANSWER
(VALUES DATE('2000-01-01')                  ==========================
 UNION  ALL                                 YEAR MIN_DT     MAX_DT
 SELECT yymmdd + 1 DAY                      ---- ---------- ----------
 FROM   temp1                               2000 2000-08-06 2000-08-12
 WHERE  yymmdd < '2010-12-31'               2001 2001-08-12 2001-08-18
)                                           2002 2002-08-11 2002-08-17
SELECT   yy                    AS year      2003 2003-08-10 2003-08-16
        ,CHAR(MIN(yymmdd),ISO) AS min_dt    2004 2004-08-08 2004-08-14
        ,CHAR(MAX(yymmdd),ISO) AS max_dt    2005 2005-08-07 2005-08-13
FROM    (SELECT yymmdd                      2006 2006-08-13 2006-08-19
               ,YEAR(yymmdd) yy             2007 2007-08-12 2007-08-18
               ,WEEK(yymmdd) wk             2008 2008-08-10 2008-08-16
         FROM   temp1                       2009 2009-08-09 2009-08-15
         WHERE  WEEK(yymmdd) = 33           2010 2010-08-08 2010-08-14
        )AS xxx
GROUP BY yy
        ,wk;
Figure 1085, Comparing week 33 over 10 years






SELECT  SUM(INTEGER(salary)) AS s1                       ANSWER
       ,INTEGER(SUM(salary)) AS s2                       =============
FROM    staff;                                           S1     S2
                                                         ------ ------
                                                         583633 583647
Figure 1086, DB2 data truncation






SELECT  SUM(INTEGER(ROUND(salary,-1))) AS s1             ANSWER
       ,INTEGER(SUM(salary)) AS s2                       =============
FROM    staff;                                           S1     S2
                                                         ------ ------
                                                         583640 583647
Figure 1087, DB2 data rounding






SELECT   lastname                                    ANSWER
        ,sex                                         =================
        ,CASE                                        LASTNAME   SX SXX
           WHEN sex >= 'F' THEN 'FEM'                ---------- -- ---
           WHEN sex >= 'M' THEN 'MAL'                JEFFERSON  M  FEM
         END AS sxx                                  JOHNSON    F  FEM
FROM     employee                                    JONES      M  FEM
WHERE    lastname LIKE 'J%'
ORDER BY 1;
Figure 1088, Case WHEN Processing - Incorrect






SELECT   lastname                                    ANSWER
        ,sex                                         =================
        ,CASE                                        LASTNAME   SX SXX
           WHEN sex >= 'M' THEN 'MAL'                ---------- -- ---
           WHEN sex >= 'F' THEN 'FEM'                JEFFERSON  M  MAL
         END AS sxx                                  JOHNSON    F  FEM
FROM     employee                                    JONES      M  MAL
WHERE    lastname LIKE 'J%'
ORDER BY 1;
Figure 1089, Case WHEN Processing - Correct






SELECT  AVG(salary) / AVG(comm) AS a1            ANSWER >>>  A1  A2
       ,AVG(salary / comm)      AS a2                        --  -----
FROM    staff;                                               32  61.98
Figure 1090, Division and Average






SELECT   hiredate                                           ANSWER
FROM     employee                                           ==========
WHERE    hiredate < '1960-01-01'                            1947-05-05
ORDER BY 1;                                                 1949-08-17
                                                            1958-05-16
Figure 1091, DATE output in year, month, day order






SELECT   CHAR(hiredate,USA)                                 ANSWER
FROM     employee                                           ==========
WHERE    hiredate < '1960-01-01'                            05/05/1947
ORDER BY 1;                                                 05/16/1958
                                                            08/17/1949
Figure 1092, DATE output in month, day, year order






EXEC-SQL
   DECLARE fred CURSOR FOR
   SELECT   *
   FROM     staff
   WHERE    id < 1000
   ORDER BY id;
END-EXEC;
       
EXEC-SQL
   OPEN fred
END-EXEC;
       
DO UNTIL SQLCODE = 100;
       
   EXEC-SQL
      FETCH fred
      INTO  :HOST-VARS
   END-EXEC;
       
   IF SQLCODE <> 100 THEN DO;
      SET HOST-VAR.ID = HOST-VAR.ID + 500;
      EXEC-SQL
         INSERT INTO staff VALUES (:HOST-VARS)
      END-EXEC;
   END-DO;
       
END-DO;
       
EXEC-SQL
   CLOSE fred
END-EXEC;
Figure 1093, Ambiguous Cursor






SELECT   region_code   AS region
        ,order_status  AS status
        ,order_number  AS order#
        ,order_value   AS value
FROM     order_table
WHERE    order_date  =  '2006-03-12'
ORDER BY region_code
        ,order_status
WITH CS;
Figure 1094, Select from ORDER table






REGION  STATUS  ORDER#  VALUE
------  ------  ------  ------
EAST    PAID       111    4.66    <----- Same ORDER#
EAST    PAID       222    6.33        |
EAST    PAID       333  123.45        |
EAST    SHIPPED    111    4.66    <---+
EAST    SHIPPED    444  123.45
Figure 1095, Sample query output






                       SAME RESULT   FETCH SAME  UNCOMMITTED  ROWS
CURSOR "WITH" OPTION   IF RUN TWICE  ROW > ONCE  ROWS SEEN    LOCKED
=====================  ============  ==========  ===========  ========
RR - Repeatable Read   Yes           Never       Never        Many/All
RS - Read Stability    No (inserts)  Never       Never        Many/All
CS - Cusor Stability   No (all DML)  Maybe       Never        Current
UR - Uncommitted Read  No (all DML)  Maybe       Yes          None
Figure 1096, WITH Option vs. Actions






SELECT   region_code   AS region
        ,order_status  AS status
        ,order_number  AS order#
        ,order_value   AS value
FROM     order_table
WHERE    order_date  =  '2006-03-12'
  AND    update_ts   <   CURRENT TIMESTAMP   <= New predicate
ORDER BY region_code
        ,order_status
WITH CS;
Figure 1097, Select from ORDER table






#1  UPDATE statement begins (will run for a long time).
#2  QUERY begins (will also run for a long time).
#3  QUERY fetches target row (via secondary index).
#4  QUERY moves on to the next row, etc...
#5  UPDATE changes target row - moves it down index.
#6  UPDATE statement finishes, and commits.
#7  QUERY fetches target row again (bother).
Figure 1098, Sequence of events required to fetch same row twice






CREATE TABLE order_table
(order#        SMALLINT    NOT NULL
,order_date    DATE        NOT NULL
,order_status  CHAR(1)     NOT NULL
,order_value   DEC(7,2)    NOT NULL
,order_rct     TIMESTAMP   NOT NULL
               GENERATED ALWAYS
               FOR EACH ROW ON UPDATE
               AS ROW CHANGE TIMESTAMP
,PRIMARY KEY   (order#));
Figure 1099, Table with ROW CHANGE TIMESTAMP column






SELECT   region_code   AS region
        ,order_status  AS status
        ,order_number  AS order#
        ,order_value   AS value
FROM     order_table
WHERE    order_date   =  '2006-03-12'
  AND    order_rct   <=   CURRENT TIMESTAMP   <= New predicate
ORDER BY region_code
        ,order_status
WITH CS;
Figure 1100, Select from ORDER table






SELECT  order#                                                  ANSWER
FROM    FINAL TABLE                                             ======
   (INSERT INTO order_table                                     order#
           (order#, order_date, order_status, order_value)      ------
    VALUES (1,'2007-11-22','A',123.45)                               1
          ,(2,'2007-11-22','A',123.99)
          ,(3,'2007-11-22','A',123.99))
WHERE   order_rct <= CURRENT TIMESTAMP;
Figure 1101, SELECT from INSERT






  SELECT   region_code   AS region
          ,order_status  AS status
          ,order_number  AS order#
          ,order_value   AS value
  FROM    (SELECT   DISTINCT
                    order_number AS distinct_order#
           FROM     order_table
           WHERE    order_date  =  '2006-03-12'
          )AS xxx
          ,order_table
  WHERE    order_number  =  distinct_order#
  ORDER BY region_code
          ,order_status
  WITH CS;
      Figure 1102, Two-part query






CREATE TABLE test_table
(test#        SMALLINT    NOT NULL
,current_ts   TIMESTAMP   NOT NULL
,generate_u   TIMESTAMP   NOT NULL
,generate_a   TIMESTAMP   NOT NULL
              GENERATED ALWAYS
              FOR EACH ROW ON UPDATE
              AS ROW CHANGE TIMESTAMP);
Figure 1103, Create table to hold timestamp values






INSERT INTO test_table (test#, current_ts, generate_u)
WITH   
temp1 (t1) AS
   (VALUES (1),(2),(3),(4)),
temp2 (t1, ts1, ts2) AS
   (SELECT  t1
           ,CURRENT TIMESTAMP
           ,TIMESTAMP(GENERATE_UNIQUE()) + CURRENT TIMEZONE
    FROM    temp1)
SELECT  *
FROM    temp2;
Figure 1104, Insert four rows






Figure 1105, Table after insert






WITH temp (f1) AS
(VALUES FLOAT(1.23456789)
 UNION ALL
 SELECT f1 * 10
 FROM   temp
 WHERE  f1 < 1E18
)      
SELECT f1             AS float1
      ,DEC(f1,31,8)   AS decimal1
      ,BIGINT(f1)     AS bigint1
FROM   temp;
Figure 1106, Multiply floating-point number by ten, SQL






Figure 1107, Multiply floating-point number by ten, answer






WITH temp (f1,f2) AS
(VALUES (FLOAT(1.23456789E1 * 10 * 10 * 10 * 10 * 10 * 10 * 10)
        ,FLOAT(1.23456789E8)))
SELECT f1
      ,f2
FROM   temp              ANSWER
WHERE  f1 <> f2;         =============================================
                         F1                     F2
                         ---------------------- ----------------------
                         +1.23456789000000E+008 +1.23456789000000E+008
Figure 1108, Two numbers that look equal, but aren't equal






WITH temp (f1,f2) AS
(VALUES (FLOAT(1.23456789E1 * 10 * 10 * 10 * 10 * 10 * 10 * 10)
        ,FLOAT(1.23456789E8)))
SELECT HEX(f1) AS hex_f1
      ,HEX(f2) AS hex_f2
FROM   temp                          ANSWER
WHERE  f1 <> f2;                     =================================
                                     HEX_F1           HEX_F2
                                     ---------------- ----------------
                                     FFFFFF53346F9D41 00000054346F9D41
Figure 1109, Two numbers that look equal, but aren't equal, shown in HEX






WITH   
 temp1 (dec1, dbl1) AS
   (VALUES (DECIMAL(1),DOUBLE(1)))
,temp2 (dec1, dec2, dbl1, dbl2) AS
   (SELECT dec1
          ,dec1 / 3 AS dec2
          ,dbl1                         ANSWER (1 row returned)
          ,dbl1 / 3 AS dbl2             ==============================
    FROM   temp1)                       DEC1 =  1.0
SELECT *                                DEC2 =  0.33333333333333333333
FROM   temp2                            DBL1 =  +1.00000000000000E+000
WHERE  dbl2 <> dec2;                    DBL2 =  +3.33333333333333E-001
Figure 1110, Comparing float and decimal division






WITH temp (f1, d1) AS
(VALUES (FLOAT(1.23456789)
        ,DEC(1.23456789,20,10))
 UNION ALL
 SELECT f1 * 10
       ,d1 * 10
 FROM   temp
 WHERE  f1 < 1E9
)      
SELECT f1
      ,d1
      ,CASE
          WHEN d1 = f1 THEN 'SAME'
          ELSE              'DIFF'
       END AS compare
FROM   temp;
Figure 1111, Comparing float and decimal multiplication, SQL






F1                      D1                     COMPARE
----------------------  ---------------------  -------
+1.23456789000000E+000           1.2345678900  SAME
+1.23456789000000E+001          12.3456789000  SAME
+1.23456789000000E+002         123.4567890000  DIFF
+1.23456789000000E+003        1234.5678900000  DIFF
+1.23456789000000E+004       12345.6789000000  DIFF
+1.23456789000000E+005      123456.7890000000  DIFF
+1.23456789000000E+006     1234567.8900000000  SAME
+1.23456789000000E+007    12345678.9000000000  DIFF
+1.23456789000000E+008   123456789.0000000000  DIFF
+1.23456789000000E+009  1234567890.0000000000  DIFF
Figure 1112, Comparing float and decimal multiplication, answer






WITH temp (f1) AS              ANSWER
(VALUES FLOAT(0.1))            =======================================
SELECT f1                      F1                     HEX_F1
      ,HEX(f1) AS hex_f1       ---------------------- ----------------
FROM   temp;                   +1.00000000000000E-001 9A9999999999B93F
Figure 1113, Internal representation of "one tenth" in floating-point






WITH temp (f1) AS
(VALUES DECFLOAT(1.23456789)
 UNION ALL
 SELECT f1 * 10
 FROM   temp
 WHERE  f1 < 1E18
)      
SELECT f1             AS float1
      ,DEC(f1,31,8)   AS decimal1
      ,BIGINT(f1)     AS bigint1
FROM   temp;
Figure 1114, Multiply DECFLOAT number by ten, SQL


