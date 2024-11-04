# Jeg er så færdig ses på 3. semester
3.3.3

GET http://localhost:7070/api/trips/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:07:00 GMT
Content-Type: application/json
Content-Length: 281
3.3.4
{
"id": 1,
"name": "City Tour",
"startTime": [
9,
0
],
"endTime": [
12,
0
],
"startPosition": "Main Square",
"price": 29.99,
"category": "city",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john@example.com",
"phonenumber": "1234567890",
"yearsOfExperience": 5,
"trips": null havde ikke tid til at rette dette
},
"guideId": null avde ikke tid til at rette dette
}
Response file saved.
> 2024-11-04T100700.200.json

Response code: 200 (OK); Time: 163ms (163 ms); Content length: 281 bytes (281 B)
_______
GET http://localhost:7070/api/trips

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:09:58 GMT
Content-Type: application/json
Content-Length: 576

[
{
"id": 1,
"name": "City Tour",
"startTime": [
9,
0
],
"endTime": [
12,
0
],
"startPosition": "Main Square",
"price": 29.99,
"category": "city",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john@example.com",
"phonenumber": "1234567890",
"yearsOfExperience": 5,
"trips": null
},
"guideId": null
},
{
"id": 2,
"name": "Mountain Adventure",
"startTime": [
8,
0
],
"endTime": [
16,
0
],
"startPosition": "Base Camp",
"price": 49.99,
"category": "forest",
"guide": {
"id": 2,
"firstName": "Jane",
"lastName": "Smith",
"email": "jane@example.com",
"phonenumber": "0987654321",
"yearsOfExperience": 3,
"trips": null
},
"guideId": null
}
]
Response file saved.
> 2024-11-04T100958.200.json

Response code: 200 (OK); Time: 225ms (225 ms); Content length: 576 bytes (576 B)
_______
PUT http://localhost:7070/api/trips/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:12:50 GMT
Content-Type: application/json
Content-Length: 300

{
"id": 1,
"name": "Updated beach Tour",
"startTime": [
10,
0
],
"endTime": [
12,
0
],
"startPosition": "Updated City Center",
"price": 109.99,
"category": "beach",
"guide": {
"id": 2,
"firstName": "Jane",
"lastName": "Smith",
"email": "jane@example.com",
"phonenumber": "0987654321",
"yearsOfExperience": 3,
"trips": null
},
"guideId": 2
}
____
POST http://localhost:7070/api/trips

HTTP/1.1 201 Created
Date: Mon, 04 Nov 2024 10:14:00 GMT
Content-Type: application/json
Content-Length: 296

{
"id": 3,
"name": "Forest Adventure Tour",
"startTime": [
9,
0
],
"endTime": [
17,
0
],
"startPosition": "Downtown Square",
"price": 89.99,
"category": "forest",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john@example.com",
"phonenumber": "1234567890",
"yearsOfExperience": 5,
"trips": null
},
"guideId": 1
}
Response file saved.
> 2024-11-04T101400.201.json

Response code: 201 (Created); Time: 10ms (10 ms); Content length: 296 bytes (296 B)
_______
DELETE http://localhost:7070/api/trips/1

HTTP/1.1 204 No Content
Date: Mon, 04 Nov 2024 10:14:51 GMT
Content-Type: text/plain

<Response body is empty>

Response code: 204 (No Content); Time: 16ms (16 ms); Content length: 0 bytes (0 B)
_______
PUT http://localhost:7070/api/trips/1/addGuide/3

HTTP/1.1 204 No Content
Date: Mon, 04 Nov 2024 10:20:46 GMT
Content-Type: text/plain

<Response body is empty>

Response code: 204 (No Content); Time: 8ms (8 ms); Content length: 0 bytes (0 B)
_______
POST http://localhost:7070/api/trips/populate

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:27:11 GMT
Content-Type: text/plain
Content-Length: 32

Database populated successfully.

Response code: 200 (OK); Time: 31ms (31 ms); Content length: 32 bytes (32 B)
_______

3.3.5

PUT bruges til at ændre en eksisterende ressource, mens POST anvendes til at oprette nye ressourcer. At tilføje en guide til en tur betragtes som en opdatering af turens tilstand.
_______
4.1

DELETE http://localhost:7070/api/trips/5

HTTP/1.1 404 Not Found
Date: Mon, 04 Nov 2024 10:28:41 GMT
Content-Type: application/json
Content-Length: 53

An unexpected error occurred while deleting the trip.
Response file saved.
> 2024-11-04T102841.404.json

Response code: 404 (Not Found); Time: 11ms (11 ms); Content length: 53 bytes (53 B)

GET http://localhost:7070/api/trips/100

HTTP/1.1 500 Server Error
Date: Mon, 04 Nov 2024 10:29:30 GMT
Content-Type: application/json
Content-Length: 52

An unexpected error occurred while reading the trip.
Response file saved.
> 2024-11-04T102930.500.json

Response code: 500 (Server Error); Time: 6ms (6 ms); Content length: 52 bytes (52 B)
_______

5.1 && 5.2

GET http://localhost:7070/api/trips/guides/totalprice/2

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:46:44 GMT
Content-Type: application/json
Content-Length: 33

{
"guideId": 2,
"totalPrice": 149.97
}
Response file saved.
> 2024-11-04T104644.200.json

GET http://localhost:7070/api/trips/category/forest

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:57:12 GMT
Content-Type: application/json
Content-Length: 1161

[
{
"id": 2,
"name": "Mountain Adventure",
"startTime": [
8,
0
],
"endTime": [
16,
0
],
"startPosition": "Base Camp",
"price": 49.99,
"category": "forest",
"guide": {
"id": 2,
"firstName": "Jane",
"lastName": "Smith",
"email": "jane@example.com",
"phonenumber": "0987654321",
"yearsOfExperience": 3,
"trips": null
},
"guideId": 2
},
{
"id": 4,
"name": "Mountain Adventure",
"startTime": [
8,
0
],
"endTime": [
16,
0
],
"startPosition": "Base Camp",
"price": 49.99,
"category": "forest",
"guide": {
"id": 2,
"firstName": "Jane",
"lastName": "Smith",
"email": "jane@example.com",
"phonenumber": "0987654321",
"yearsOfExperience": 3,
"trips": null
},
"guideId": 2
},
{
"id": 6,
"name": "Mountain Adventure",
"startTime": [
8,
0
],
"endTime": [
16,
0
],
"startPosition": "Base Camp",
"price": 49.99,
"category": "forest",
"guide": {
"id": 2,
"firstName": "Jane",
"lastName": "Smith",
"email": "jane@example.com",
"phonenumber": "0987654321",
"yearsOfExperience": 3,
"trips": null
},
"guideId": 2
},
{
"id": 8,
"name": "Mountain Adventure",
"startTime": [
8,
0
],
"endTime": [
16,
0
],
"startPosition": "Base Camp",
"price": 49.99,
"category": "forest",
"guide": {
"id": 2,
"firstName": "Jane",
"lastName": "Smith",
"email": "jane@example.com",
"phonenumber": "0987654321",
"yearsOfExperience": 3,
"trips": null
},
"guideId": 2
}
]
Response file saved.
> 2024-11-04T105712.200.json

Response code: 200 (OK); Time: 169ms (169 ms); Content length: 1161 bytes (1,16 kB)
_______

8.3

For at løse fejlen med 401 Unauthorized i mine tests, vil jeg sikre, at de anvender gyldige autentificeringstokens, der matcher de roller, der kræves for de beskyttede slutpunkter. Jeg vil opdatere testkoden, så den inkluderer logik til at hente disse tokens, inden testene udføres.