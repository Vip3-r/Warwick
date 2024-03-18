/*
 * KPI look up with jQuery by Adil Khalifa.
 */
(function($){
    'use strict';

    const ERROR_NOT_FOUND = 'Failed to load data resource.';

    const UNITS = {
        "wacc" :  "%",
        "scores" : "",
        "factory_utilization" : "%",
        "employee_engagement" : "%",
        "interest_coverage" : "x",
        "marketing_spend_rev" : " USD",
        "e_cars_sales" : " units",
        "co2_penalty" : "M (USD)"
    }

    const TEAM_NAMES = ["fovro", "Fastun", "Nyxx", "CarSpa", "Motion", "Worthwheel", "Carzio", "Rollovo", "iAuto", "VroomTime", "Kar", "EliteTechs", "Carz", "MileMode", "Automotiq", "RYDI", "EvolutionAuto", "Automovo", "ROBOH", "rimovo", "ottobi", "Evi", "Rusted", "Cjio", "NitroRide", "HXH", "SpeedLabs", "TenQ", "Caraxa", "Blazers", "DriveSwitch", "GIIQ", "Teuso", "Hoqa", "AutoInfinite", "vusk", "DentCenter", "Turbo", "evCU", "Electronically", "Drivat", "Torque", "Drift", "Carvato", "Rush", "Matic", "Wheelic", "Slidyn", "Pitpo", "caralo", "Drivesly", "Xuad", "CarLeap", "Tazox", "Amxu", "Honkli"];

    const data = new Map();

    const init = () => {
        const statusElement = $('.request-status');
        const dateOptions = $('#date');
        $.get('/rankings').done((result) => {
            const sortByDate = result.sort((a, b) => new Date(a.date) - new Date(b.date));
            dateOptions.empty();
            sortByDate.forEach((row, index) => {
                data.set(row.date, row);
                dateOptions.append(`<option value="${row.date}">Day ${index + 1}</option>`);
            });
            dateOptions.val(result[0].date);
            statusElement.hide();
            update();
        }).error(() => {
            statusElement.text(ERROR_NOT_FOUND);
            statusElement.show();
        });
        bindListeners();
    }

    const update = () => {
        $("#datatable tbody").empty();
        const wantedDate = $('#date').val();
        const wantedMetric = $('#kpi').val();
        const kpiData = data.get(wantedDate);
        var rows = kpiData[wantedMetric].sort((a, b) => b.value - a.value);

        var rank = 1;
        var last = null;
        $.each(rows, function(index, row) {
            $("#datatable tbody").append("<tr><td>" + (row.value != last ? rank : '=') + "</td><td>" + TEAM_NAMES[row.team - 1] + "</td><td>" + row.value + UNITS[$("#kpi").val()] + "</td></tr>")
            last = row.value;
            rank = rank + 1;
        });
    }

    function bindListeners() {
        $('#date').on('change', (event) => update());
        $('#kpi').on('change', (event) => update());
    }

    $(document).ready(() => init());
})(jQuery)
