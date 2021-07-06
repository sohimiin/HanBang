const express = require('express');
const app = express();

app.listen(3000, () => {
                        console.log('DBconnecting listening on port 3000!');
});

app.post('/write', function(req, res, next){
        console.log('who get in here post /write');
                inputData = JSON.parse(data);
        });

        req.on('end', () => {
                //id = inputData.ID;
                floor = inputData.Floor;
                elevator = inputData.Elevator;
                refrigerator = inputData.Refrigerator;
                washer = inputData.Washer;
                closet = inputData.Closet;
                induction = inputData.Induction;
                shoecloset = inputData.Shoecloset;
                veranda = inputData.Veranda;
                wifi = inputData.Wifi;
                smoke = inputData.Smoke;
                loc = inputData.Location;
                deposit = inputData.Deposit;
                yearrent = inputData.Yearrent;
                id = inputData.id;
                monthlyrent = inputData.MonthlyRent;
                roomspace = inputData.Roomspace;
                maintencecost = inputData.Maintencecost;
                rooms = inputData.Rooms;
                timeregister = inputData.TimeRegister;
                //pictures = inputData.Pictures;

                console.log(timeregister + ", " + floor + ", " +  monthlyrent + ", " + maintencecost + ", " + roomspace + ", " + elevator + ", " + refrigerator + ", " + washer + ", " + microwave + ", " + closet + ", " + induction + ", " + shoecloset + ", " + veranda + ", " + wifi + ", " + smoke + ", " + desk + ", " + airconditioner + ", " + deposit + ", " + rooms + ", " + yearrent + ", " + texts);
                var mysql = require('mysql');

                var connection = mysql.createConnection({
                        host : 'localhost',
                        port : 3306,
                        user : 'root',
                        password : 'lllhy18',
                        database : 'HanBang'
                });

                var sql = "INSERT INTO Room VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                var params = [loc, floor, monthlyrent, maintencecost, roomspace, elevator, refrigerator, washer, microwave, closet, induction, shoecloset, veranda, wifi, smoke, desk, timeregister, airconditioner, deposit, rooms, yearrent, "thgml", texts, "banana.jpg", 0];
                connection.connect();
                connection.query(sql, params, function(err, results, field){
                        console.log(results);
                });
        });
});
