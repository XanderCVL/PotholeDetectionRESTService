create table Markers (
    ID BigSerial PRIMARY KEY,
    -- Coordinates geography(Point) UNIQUE NOT NULL,
    N_Detections INTEGER CHECK (N_Detections >= 0) default 1,
    Country VarChar(60) NOT NULL,
    Country_Code VarChar(10) NOT NULL,
    Region VarChar(50) NOT NULL,
    County VarChar(50),
    City VarChar(50),
    District VarChar(50),
    Suburb VarChar(50),
    Town VarChar(100) NOT NULL,
    Village VarChar(50),
    Place VarChar(50),
    Postcode VarChar(10),
    Neighbourhood VarChar(50),
    Road VarChar(50) NOT NULL,
    House_Number VarChar(10)
);

SELECT AddGeometryColumn(
  'markers',
  'coordinates',
  4326,
  'POINT',
  2
);

create table Comments (
    ID BigSerial PRIMARY KEY,
    marker_id BIGINT REFERENCES Markers(ID),
    text VarChar(200),
    posting_date DATE NOT NULL DEFAULT CURRENT_DATE
);

SELECT
    ID,
    json_build_object(
        'country',country,
        'countryCode',country_code,
        'region',region,
        'county',county,
        'city',city,
        'district',district,
        'suburb',suburb,
        'town',town,
        'village',village,
        'place',place,
        'neighbourhood',neighbourhood,
        'road',road
    ) AS addressNode,
    ST_AsGeoJSON(coordinates)::json->'coordinates' AS coordinates
FROM markers;

SELECT
    ID,
	json_build_object(
        'country',country,
        'countryCode',country_code,
        'region',region,
        'county',county,
        'city',city,
        'district',district,
        'suburb',suburb,
        'town',town,
        'village',village,
        'place',place,
        'neighbourhood',neighbourhood,
        'road',road
    ) AS addressNode,
	ST_AsGeoJSON(coordinates)::json->'coordinates' AS coordinates
FROM markers
WHERE ST_DistanceSphere(
	ST_SetSRID(ST_MakeLine(ST_MakePoint(latA, lngA), ST_MakePoint(latB, lngB)), 4326), -- Distanza retta-punto
	markers.coordinates
) < 100;

SELECT
    ID,
    json_build_object(
        'country',country,
        'countryCode',country_code,
        'region',region,
        'county',county,
        'city',city,
        'district',district,
        'suburb',suburb,
        'town',town,
        'village',village,
        'place',place,
        'neighbourhood',neighbourhood,
        'road',road
    ) AS addressNode,
    ST_AsGeoJSON(coordinates)::json->'coordinates' AS coordinates
FROM markers
WHERE
    markers.coordinates &&
    ST_Transform(
        ST_MakeEnvelope(maxLat, maxLng, minLat, minLng, 4326), -- Bounding Box
        4326
    );

INSERT INTO Comments(MID, text) VALUES (0,"commento");

SELECT
    ID,
	json_build_object(
        'country',country,
        'countryCode',country_code,
        'region',region,
        'county',county,
        'city',city,
        'district',district,
        'suburb',suburb,
        'town',town,
        'village',village,
        'place',place,
        'neighbourhood',neighbourhood,
        'road',road
    ) AS addressNode,
	ST_AsGeoJSON(coordinates)::json->'coordinates' AS coordinates
FROM markers
WHERE ST_DistanceSphere(
	ST_SetSRID(ST_MakeLine(ST_MakePoint(latA, lngA)), 4326), -- Distanza punto-punto
	markers.coordinates
) < radius;


--"SELECT " +
--"ID," +
--"json_build_object(" +
--"'country',country," +
--"'countryCode',country_code," +
--"'region',region," +
--"'county',county," +
--"'town',town," +
--"'place',place," +
--"'neighbourhood',neighbourhood," +
--"'road',road" +
--") AS addressNode," +
--"ST_AsGeoJSON(coordinates)::json->'coordinates' AS coordinates" +
--"FROM markers " +
--"WHERE markers.coordinates && " +
--"ST_Transform(" +
--"ST_MakeEnvelope(:min_lat, :min_lng, :max_lat, :max_lng, 4326)," +
--"4326" + //SRID
--");"
