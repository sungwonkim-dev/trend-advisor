
var defaultData = [];
var labels = [];

$(document).ready(function(){
    var data = {"word":document.getElementById("word").value};
    $.ajax({
        method: 'GET',
        url: '/api/chart/data',
        data: data,
        success: function (data) {
            defaultData = data.first_data;
            labels = data.labels;
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
    var lineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: '# of Votes',
                label: "검색 데이터",
                fill: "start",
                backgroundColor: "rgba(220,220,220,0.2)",
                pointBackgroundColor: "rgba(151,187,205,1)",
                borderColor: "rgba(151,187,205,1)",
                data: defaultData,
            }]
        },
        options:{
            scales:{
                yAxes:[{
                    ticks:{
                        reverse:true,
                        min:1,
                        max:500,
                    }
                }]
            }
        }
        
    });
}
