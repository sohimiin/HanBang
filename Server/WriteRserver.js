const express = require('express');
const app = express();

app.listen(3000, () => {
                        console.log('DBconnecting listening on port 3000!');
});
var timeregister, text, ownergb, heatcooling, optionsoperation, soundproof, waterpressure, cctv, id, rating, apictures;

app.post('/review', function(req, res, next){
        console.log('who get in here post /review');
        var inputData;

        req.on('data', (data) => {
                inputData = JSON.parse(data);
        });

        req.on('end', () => {
                text = inputData.Text;
                ownergb = inputData.OwnerGB;
                heatcooling = inputData.HeatCooling;
                optionsoperation = inputData.Optionsoperation;
                soundproof = inputData.Soundproof;
                waterpressure = inputData.Waterpressure;
                cctv = inputData.CCTV;
                timeregister = inputData.TimeRegister;
                //id = inputData.id;
                rating = inputData.Rating;

                //console.log(text + ", " + ownergb + ", " + heatcooling + ", " + optionsoperation + ", " + soundproof + ", " + waterpressure + ", " + cctv + ", " + timeregister + ", " + rating);
                var mysql = require('mysql');

                var connection = mysql.createConnection({
                        host : 'localhost',
                        port : 3306,
                        user : 'root',
                        password : 'lllhy18',
                        database : 'HanBang'
                });

                var sql = "INSERT INTO Review VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                var params = [timeregister, ownergb, heatcooling, optionsoperation, soundproof, waterpressure, cctv, "thgml", rating, "apple.jpg", text];
                connection.connect();
                connection.query(sql, params, function(err, results, field){
                        console.log(results);
                });
        });
});

