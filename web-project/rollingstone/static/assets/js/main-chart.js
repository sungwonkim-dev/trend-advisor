
var defaultData = [1,2,3,4,5,6,7];
var labels = [];
var labelname;
var datasets = [];

function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

function keywordData(data)
{
    var data = {"word":data.name};
    console.log(data);
    $.ajax({
        method:'GET',
        url: '/api/chart/data',
        data:data,
        success: function(data){
            defaultData = data.first_data;
            labels = data.labels;
            labelname = data.word;
            var randomcolor = getRandomColor();
            var data = {
                label: '# of Votes',
                label: labelname,
                fill: "start",
                backgroundColor: "rgba(220,220,220,0.2)",
                pointBackgroundColor: randomcolor,
                borderColor: randomcolor,
                data: defaultData,
            };
            datasets.push(data);
            setChart();
            var offset = $('#keyword-graph').offset();
            $('html, body').animate({scrollTop:offset.top}, 1000);
        },
        error: function(data){
            console.log("Error");
        }
    })
}

$(document).ready(function(){
    var data = {"word":document.getElementById("word").value};
    $.ajax({
        method: 'GET',
        url: '/api/chart/data',
        data: data,
        success: function (data) {
            defaultData = data.first_data;
            labels = data.labels;
            labelname = data.word;
            setChart();
        },
        error: function (data) {
            console.log("Error");
            console.log(data);
        }
    });
})


function setChart() {
    var ctx = document.getElementById("graph");
    var setting = {
        type: 'line',
        data: {
            labels: labels,
            datasets: datasets,
        },
        options:{
            scales:{
                yAxes:[{
                    ticks:{
                        reverse:true,
                        min:1,
                        max:40,
                    }
                }]
            }
        }
    }
    var lineChart = new Chart(ctx, setting);
}
