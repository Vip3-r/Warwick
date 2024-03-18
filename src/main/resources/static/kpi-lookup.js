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

    const LABELS = {
        "wacc" :  "WACC",
        "scores" : "Scores",
        "factory_utilization" : "Factory Utilisation",
        "employee_engagement" : "Employee Engagement",
        "interest_coverage" : "Interest Coverage",
        "marketing_spend_rev" : " Cumulative Marketing Spend",
        "e_cars_sales" : "eCar Sales",
        "co2_penalty" : "CO2 Penalty"
    }

    const TEAM_NAMES = ["fovro", "Fastun", "Nyxx", "CarSpa", "Motion", "Worthwheel", "Carzio", "Rollovo", "iAuto", "VroomTime", "Kar", "EliteTechs", "Carz", "MileMode", "Automotiq", "RYDI", "EvolutionAuto", "Automovo", "ROBOH", "rimovo", "ottobi", "Evi", "Rusted", "Cjio", "NitroRide", "HXH", "SpeedLabs", "TenQ", "Caraxa", "Blazers", "DriveSwitch", "GIIQ", "Teuso", "Hoqa", "AutoInfinite", "vusk", "DentCenter", "Turbo", "evCU", "Electronically", "Drivat", "Torque", "Drift", "Carvato", "Rush", "Matic", "Wheelic", "Slidyn", "Pitpo", "caralo", "Drivesly", "Xuad", "CarLeap", "Tazox", "Amxu", "Honkli"];

    var data = new Map();
    var accessKey;

    const init = () => {
        const table = $('.kpi-table');
        table.hide();
        bindListeners();
    }


    const loadData = () => {
        const statusElement = $('.request-status');
        const dateOptions = $('#date');
        const metricOptions = $('#kpi');

        $.ajax({
            url: '/rankings',
            dataType: 'json',
            type: 'GET',
            contentType: 'application/json; charset=utf-8',
            headers: {
                'Authorization' : `Bearer ${accessKey}`
            },
            success: (result) => {
                const sortByDate = result.sort((a, b) => new Date(a.date) - new Date(b.date));
                metricOptions.empty();
                Object.keys(result[0]).filter(key => key !== 'date').forEach(key =>
                    metricOptions.append(`<option value="${key}">${LABELS[key]}</option>`)
                );
                dateOptions.empty();
                sortByDate.forEach((row, index) => {
                    data.set(row.date, row);
                    dateOptions.append(`<option value="${row.date}">Day ${index + 1}</option>`);
                });
                dateOptions.val(result[0].date);
                statusElement.hide();
                update();
            },
            error: () => {
                statusElement.text(ERROR_NOT_FOUND);
                statusElement.show();
            }
        });
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

    const login = (path) => {
        $.get(path).done((result) => {
            accessKey = result.token;
            const login = $('.Login');
            login.hide();
            const table = $('.kpi-table');
            table.show();
            loadData();
        })
    }

    const logout = () => {
        accessKey = undefined;
        data = new Map();
        const table = $('.kpi-table');
        table.hide();
        const login = $('.Login');
        login.show();
    }

    function bindListeners() {
        $('#date').on('change', () => update());
        $('#kpi').on('change', () => update());
        $('#student-login').on('click', () => login('/auth/student'));
        $('#academic-login').on('click', () => login('/auth/academic'));
        $('#staff-login').on('click', () => login('/auth/staff'));
        $('#logout').on('click', () => logout());
    }

    $(document).ready(() => init());
})(jQuery)
