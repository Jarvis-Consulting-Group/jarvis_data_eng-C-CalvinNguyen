/*
Modifying Data
-- 1.
*/

INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (9, 'Spa', 20, 30, 100000, 800);

-- 2.

INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (
        SELECT
            COUNT(*)
        FROM
            cd.facilities,
            'Spa',
            20,
            30,
            100000,
            800
    );

-- 3.

UPDATE
    cd.facilities
SET
    initialoutlay = 10000
WHERE
    name like 'Tennis Court%'
    AND initialoutlay = 8000;

-- 4.

UPDATE
    cd.facilities
SET
    membercost = (
        SELECT
            membercost * 1.1
        FROM
            cd.facilities
        WHERE
            name = 'Tennis Court 1'
    ),
    guestcost = (
        SELECT
            guestcost * 1.1
        FROM
            cd.facilities
        WHERE
            name = 'Tennis Court 1'
    )
WHERE
    name like 'Tennis Court 2';

-- 5.

DELETE FROM
    cd.bookings;

-- 6.

DELETE FROM
    cd.members
WHERE
    memid = 37;

/*
Basics
-- 1.
*/

SELECT
    facid,
    name,
    membercost,
    monthlymaintenance
FROM
    cd.facilities
WHERE
    membercost > 0
    and membercost < (monthlymaintenance / 50);

-- 2.

SELECT
    *
FROM
    cd.facilities
WHERE
    name LIKE '%Tennis%';

-- 3.

SELECT
    *
FROM
    cd.facilities
WHERE
    facid IN(1, 5);

-- 4.

SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
    joindate >= '2012-09-01';

-- 5.

SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name
FROM
    cd.facilities;

/*
Joins
-- 1.
*/

SELECT
    boo.starttime
FROM
    cd.bookings AS boo
    JOIN cd.members AS mem ON boo.memid = mem.memid
WHERE
    mem.firstname = 'David'
    AND mem.surname = 'Farrell';

-- 2.

SELECT
    boo.starttime,
    fac.name
FROM
    cd.bookings AS boo
    JOIN cd.facilities AS fac ON boo.facid = fac.facid
WHERE
    fac.name LIKE '%Tennis Court%'
    AND boo.starttime >= '2012-09-21'
    AND boo.starttime < '2012-09-22';

-- 3.

SELECT
    mem.firstname AS memfname,
    mem.surname AS memsname,
    rec.firstname AS recfname,
    rec.surname AS recsname
FROM
    cd.members AS mem
    LEFT OUTER JOIN cd.members as rec ON rec.memid = mem.recommendedby
ORDER BY
    memsname,
    memfname;

-- 4.

SELECT
    DISTINCT rec.firstname AS recfname,
    rec.surname AS recsname
FROM
    cd.members AS mem
    JOIN cd.members as rec ON rec.memid = mem.recommendedby
ORDER BY
    recsname,
    recfname;

-- 5.

SELECT
    DISTINCT (firstname || ' ' || surname) AS member,
    (
     SELECT
         (firstname || ' ' || surname)
     FROM
         cd.members AS mem2
     WHERE
         mem1.recommendedby = mem2.memid
    ) AS recommender
FROM
    cd.members AS mem1
ORDER BY
    1;

/*
Aggregation
-- 1.
*/

SELECT
    recommendedby,
    COUNT(*)
FROM
    cd.members
WHERE
    recommendedby IS NOT NULL
GROUP BY
    recommendedby
ORDER BY
    recommendedby;

-- 2.

SELECT
    facid,
    SUM(slots) AS "Total Slots"
FROM
    cd.bookings
GROUP BY
    facid
ORDER BY
    facid;

-- 3.

SELECT
    facid,
    SUM(slots) AS "Total Slots"
FROM
    cd.bookings
WHERE
    starttime >= '2012-09-01'
    AND starttime < '2012-10-01'
GROUP BY
    facid
ORDER BY
    2;

-- 4.

SELECT
    facid,
    EXTRACT(
            'MONTH'
            FROM
            starttime
        ) AS month,
    SUM(slots) AS "Total Slots"
FROM
    cd.bookings
WHERE
    starttime >= '2012-01-01'
    AND starttime <= '2012-12-31'
GROUP BY
    facid,
    2
ORDER BY
    1,
    2;

-- 5.

SELECT
    COUNT(DISTINCT memid)
FROM
    cd.bookings;

-- 6.

SELECT
    mem.surname,
    mem.firstname,
    boo.memid,
    MIN(boo.starttime)
FROM
    cd.bookings AS boo
    JOIN cd.members AS mem ON mem.memid = boo.memid
WHERE
        boo.starttime >= '2012-09-01'
GROUP BY
    mem.surname,
    mem.firstname,
    boo.memid
ORDER BY
    boo.memid;

-- 7.

SELECT
    COUNT(memid) OVER(),
        firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate;

-- 8.

SELECT
    ROW_NUMBER() OVER(),
        firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate;

-- 9.

SELECT
    facid,
    total
FROM
    (
        SELECT
            facid,
            sum(slots) AS total,
            rank() over (
        ORDER BY
          SUM(slots) DESC
      ) AS rank
        FROM
            cd.bookings
        GROUP BY
            facid
    ) AS ranked
WHERE
        rank = 1;

/*
String
-- 1.
*/

SELECT
    surname || ', ' || firstname AS name
FROM
    cd.members;

-- 2.

SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone LIKE '%(___)%';

-- 3.

SELECT
    SUBSTR(surname, 1, 1),
    count(*)
FROM
    cd.members
GROUP BY
    1
ORDER BY
    1;