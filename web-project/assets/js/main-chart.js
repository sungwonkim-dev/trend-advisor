

// dataset 속성과 option속성은 다음 링크를 참조
// (https://www.chartjs.org/docs/latest/charts/line.html)

function linegraph() {
    var randomScalingFactor = function () { return Math.round(Math.random() * 100) };
    var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    var lineChart = null;
    var lineChartData = {
        labels: ["January", "February", "March", "April", "May", "June", "July"],
        datasets: [
            {
                label: "검색 데이터",
                fill: true,
                backgroundColor:"rgba(220,220,220,0.2)",
                pointBackgroundColor:"rgba(151,187,205,1)",
                borderColor:"rgba(151,187,205,1)",
                data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()]
            },
            {
                label: "추천 데이터",
                fill: true,
                backgroundColor:"rgba(200,150,205,0.2)",
                pointBackgroundColor:"rgba(200,150,205,1)",
                borderColor:"rgba(200,150,205,1)",
                data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()]
            }
        ]

    };
    var ctx = document.getElementById("graph").getContext("2d");
    lineChart = new Chart(ctx, {
        type: "line",
        data: lineChartData,

        // 이전 버전의 옵션들입니다.
        // ChartJS2에 알맞게 옵션들을 수정해주어야 합니다.
        options: {
            ///Boolean - Whether grid lines are shown across the chart
            scaleShowGridLines: true,
            //String - Colour of the grid lines
            scaleGridLineColor: "rgba(0,0,0,0.05)",
            //Number - Width of the grid lines
            scaleGridLineWidth: 1,
            //Boolean - Whether the line is curved between points
            bezierCurve: true,
            //Number - Tension of the bezier curve between points
            bezierCurveTension: 0.4,
            //Boolean - Whether to show a dot for each point
            pointDot: true,
            //Number - Radius of each point dot in pixels
            pointDotRadius: 4,
            //Number - Pixel width of point dot stroke
            pointDotStrokeWidth: 1,
            //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
            pointHitDetectionRadius: 20,
            //Boolean - Whether to show a stroke for datasets
            datasetStroke: true,
            //Number - Pixel width of dataset stroke
            datasetStrokeWidth: 2,
            //Boolean - Whether to fill the dataset with a colour
            datasetFill: true,
            onAnimationProgress: function () {
                console.log("onAnimationProgress");
            },
            onAnimationComplete: function () {
                console.log("onAnimationComplete");
            }
        }
    })
};
linegraph();