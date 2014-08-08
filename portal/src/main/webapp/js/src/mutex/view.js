/*
 * Copyright (c) 2012 Memorial Sloan-Kettering Cancer Center.
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 * documentation provided hereunder is on an "as is" basis, and
 * Memorial Sloan-Kettering Cancer Center
 * has no obligations to provide maintenance, support,
 * updates, enhancements or modifications.  In no event shall
 * Memorial Sloan-Kettering Cancer Center
 * be liable to any party for direct, indirect, special,
 * incidental or consequential damages, including lost profits, arising
 * out of the use of this software and its documentation, even if
 * Memorial Sloan-Kettering Cancer Center
 * has been advised of the possibility of such damage.  See
 * the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */

/**
 * 
 * Render the datatable for the mutex tab
 *
 * @Author: yichao
 * @Date: Jul 2014
 *
 **/

 var MutexView = (function() {

 	var mutexTableInstance = "",
        mutexTableDataArr = [],
        names = {
            tabldId: "mutex-table",
            divId: "mutex-table-div"
        },
        index = {
            geneA : 0,
            geneB : 1,
            pVal : 2,
            oddsRatio : 3,
            association: 4
        },
        colorCode = {
            oddsRatio: "#296CCF",
            pVal: "#296CCF"
        }

    function configTable() {
        $("#mutex-table-div").append(
            "<table id='" + names.tableId + "'>" +
            "<thead style='font-size:70%'>" +
            "<th>Gene A</th>" +
            "<th>Gene B</th>" +
            "<th>p-Value<img src='images/help.png' id='p-value-help'></th>" + 
            "<th>Log Odds Ratio<img src='images/help.png' id='odds-ratio-help'></th>" +
            "<th>Association<img src='images/help.png' id='association-help'></th>" + 
            "</thead>" +
            "<tbody></tbody>" + 
            "</table>"
        );

        mutexTableInstance = $("#" + names.tableId).dataTable({
            "sDom": '<"H"f<"mutex-table-filter">>t<"F"i>',
            "bPaginate": false,
            "sScrollY": "600px",
            "paging": false,
            "scrollCollapse": true,
            "bScrollCollapse": true,
            "bInfo": true,
            "bJQueryUI": true,
            "bAutoWidth": false,
            "aaData" : mutexTableDataArr,
            "aaSorting": [[2, 'asc']], //sort by p-Value
            "aoColumnDefs": [
                {
                    "bSearchable": true,
                    "aTargets": [ index.geneA ],
                    "sWidth": "100px"
                },
                {
                    "bSearchable": true,
                    "aTargets": [ index.geneB ],
                    "sWidth": "100px"
                },
                {
                    "sType": 'mutex-p-value',
                    "bSearchable": false,
                    "aTargets": [ index.pVal ],
                    "sWidth": "150px",
                    "sClass": "classMutexTable"

                },
                {
                    "sType": 'mutex-odds-ratio',
                    "bSearchable": false,
                    "aTargets": [ index.oddsRatio ],
                    "sWidth": "150px",
                    "sClass": "classMutexTable"
                },
                {
                    "bSearchable": false,
                    "aTargets": [ index.association ],
                    "sWidth": "500px"
                }
            ],
            "oLanguage": {
                "sSearch": "Search Gene"
            },
            "bDestory": true,
            "fnRowCallback": function(nRow, aData) {
                $('td:eq(' + index.geneA + ')', nRow).css("font-weight", "bold");
                $('td:eq(' + index.geneB + ')', nRow).css("font-weight", "bold");
                $('td:eq(' + index.pVal + ')', nRow).css("color", colorCode.pVal);
                $('td:eq(' + index.oddsRatio + ')', nRow).css("color", colorCode.oddsRatio);
                if (aData[index.association].indexOf("Significant") !== -1) { //significate p value
                    $('td:eq(' + index.pVal + ')', nRow).css("font-weight", "bold");
                }
            }
        }); 

    }

    function convertData() {
    	$.each(MutexData.getDataArr(), function(index, obj){
            if (obj.log_odds_ratio !== "--") {
                var _arr = [];
                _arr.push(obj.geneA);
                _arr.push(obj.geneB); 
                if (obj.p_value < 0.001) {
                    _arr.push("<0.001");
                } else {
                    _arr.push(obj.p_value);
                }
                if (obj.log_odds_ratio === 4) {
                    _arr.push(">3");
                } else if (obj.log_odds_ratio === -4) {
                    _arr.push("<-3");
                } else {
                    _arr.push(obj.log_odds_ratio);
                }
                _arr.push(obj.association);
                mutexTableDataArr.push(_arr);       
            }
    	});
    }

    function overWriteFilters() {
        jQuery.fn.dataTableExt.oSort['mutex-odds-ratio-desc'] = function(a,b) {
            if (a == "<-3") { a = -3 };
            if (b == "<-3") { b = -3 };
            if (a == ">3") { a = 3 };
            if (b == ">3") { b = 3 };
            if (a > b) return -1;
            else if (a < b) return 1;
            else return 0;
        };
        jQuery.fn.dataTableExt.oSort['mutex-odds-ratio-asc'] = function(a,b) {
            if (a == "<-3") { a = -3 };
            if (b == "<-3") { b = -3 };
            if (a == ">3") { a = 3 };
            if (b == ">3") { b = 3 };
            if (a > b) return 1;
            else if (a < b) return -1;
            else return 0;
        };
        jQuery.fn.dataTableExt.oSort['mutex-p-value-desc'] = function(a,b) {
            if (a == "<0.001") { a = 0.0009 };
            if (b == "<0.001") { b = 0.0009 };
            if (a > b) return -1;
            else if (a < b) return 1;
            else return 0;
        };
        jQuery.fn.dataTableExt.oSort['mutex-p-value-asc'] = function(a,b) {
            if (a == "<0.001") { a = 0.0009 };
            if (b == "<0.001") { b = 0.0009 };
            if (a > b) return 1;
            else if (a < b) return -1;
            else return 0;
        };

    }

    function attachFilter() { 

        $("#mutex-table-div").find('.mutex-table-filter').append(
            "<input type='checkbox' class='mutex-table-checkbox' checked id='mutex-table-checkbox-mutex'>Mutual exclusive</option> &nbsp;&nbsp;" +
            "<input type='checkbox' class='mutex-table-checkbox' checked id='mutex-table-checkbox-co-oc'>Co-occurrence</option> &nbsp;&nbsp;" +
            "<input type='checkbox' class='mutex-table-checkbox' id='mutex-table-checkbox-sig-only'>Significant pairs</option> &nbsp; &nbsp;"
        );

        var _sig_only_all_fn = function() {
                mutexTableInstance.fnFilter("", index.association);
                mutexTableInstance.fnFilter("", index.oddsRatio);
                mutexTableInstance.fnFilter("Significant", index.association);
            },
            _sig_only_mutex_fn = function() {
                mutexTableInstance.fnFilter("", index.association);
                mutexTableInstance.fnFilter("", index.oddsRatio);
                mutexTableInstance.fnFilter("-", index.oddsRatio, false);
                mutexTableInstance.fnFilter("Significant", index.association);
            },
            _sig_only_co_oc_fn = function() {
                mutexTableInstance.fnFilter("", index.association);
                mutexTableInstance.fnFilter("", index.oddsRatio);
                mutexTableInstance.fnFilter('^[+]?([1-9][0-9]*(?:[\.][0-9]*)?|0*\.0*[1-9][0-9]*)(?:[eE][+-][0-9]+)?$', index.oddsRatio, true);
                mutexTableInstance.fnFilter("Significant", index.association);
            },
            _mutex_fn = function() {
                mutexTableInstance.fnFilter("", index.association);
                mutexTableInstance.fnFilter("", index.oddsRatio);
                mutexTableInstance.fnFilter("-", index.oddsRatio, false);
            },
            _co_oc_fn = function() {
                mutexTableInstance.fnFilter("", index.association);
                mutexTableInstance.fnFilter("", index.oddsRatio);
                mutexTableInstance.fnFilter('^[+]?([1-9][0-9]*(?:[\.][0-9]*)?|0*\.0*[1-9][0-9]*)(?:[eE][+-][0-9]+)?$', index.oddsRatio, true);
            }, 
            _all_fn = function() {
                mutexTableInstance.fnFilter("", index.association);
                mutexTableInstance.fnFilter("", index.oddsRatio);
            },
            _empty_fn = function() {
                mutexTableInstance.fnFilter("&", index.oddsRatio);
            };

        $(".mutex-table-checkbox").change(function () {
            var _mutex_checked = false,
                _co_oc_checked = false,
                _sig_only_checked = false;

            if ($("#mutex-table-checkbox-mutex").is(':checked')) _mutex_checked = true;
            if ($("#mutex-table-checkbox-co-oc").is(':checked')) _co_oc_checked = true;
            if ($("#mutex-table-checkbox-sig-only").is(':checked')) _sig_only_checked = true;

            if (_mutex_checked && _co_oc_checked) {
                if (_sig_only_checked) _sig_only_all_fn();
                else _all_fn();
            } else if (_mutex_checked && !_co_oc_checked) {
                if (_sig_only_checked) _sig_only_mutex_fn();
                else _mutex_fn();
            } else if (!_mutex_checked && _co_oc_checked) {
                if (_sig_only_checked) _sig_only_co_oc_fn();
                else _co_oc_fn();
            } else {
                _empty_fn();
            }
        });
    }

    function addHeaderQtips() {
        $("#association-help").qtip({
            content: { text:'Log odds ratio > 0&nbsp;&nbsp;&nbsp;: Association towards co-occurrence<br>' +
                            'Log odds ratio <= 0&nbsp;: Association towards mutual exclusivity<br>' + 
                            'p-Value < 0.05&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: Significate association'},
            style: { classes: 'ui-tooltip-light ui-tooltip-rounded ui-tooltip-shadow ui-tooltip-lightyellow qtip-ui-wide'},
            show: {event: "mouseover"},
            hide: {fixed:true, delay: 100, event: "mouseout"},
            position: {my:'left bottom',at:'top right',viewport: $(window)}
        });  
        $("#odds-ratio-help").qtip({
            content: { text:'Quantifies how strongly the presence or absence of alterations in gene A are associated with the presence or absence of alterations in gene B in the selected tumors.'},
            show: {event: "mouseover"},
            hide: {fixed:true, delay: 100, event: "mouseout"},
            position: {my:'left bottom',at:'top right',viewport: $(window)}
        });  
        $("#p-value-help").qtip({
            content: { text:'Derived from Fisher Exact Test'},
            style: { classes: 'ui-tooltip-light ui-tooltip-rounded ui-tooltip-shadow ui-tooltip-lightyellow qtip-ui-wide'},
            show: {event: "mouseover"},
            hide: {fixed:true, delay: 100, event: "mouseout"},
            position: {my:'left bottom',at:'top right',viewport: $(window)}
        });  
    }

    function addStatInfo() {
        var _stat = MutexData.getDataStat();
        for (var key in _stat) {
            if (_stat[key] === 0) {
                _stat[key] = "none"; //replace 0 (text) with "non"
            }
        }
        $("#num_of_mutex").append(_stat.num_of_mutex);
        $("#num_of_sig_mutex").append(_stat.num_of_sig_mutex);
        $("#num_of_co_oc").append(_stat.num_of_co_oc);
        $("#num_of_sig_co_oc").append(_stat.num_of_sig_co_oc);
        $("#num_of_no_association").append(_stat.num_of_no_association);
    }

 	return {
 		init: function() {
 			$("#mutex-loading-image").hide();
 			convertData();
            overWriteFilters();
 			configTable();
            attachFilter();
            addHeaderQtips();
            addStatInfo();
            mutexTableInstance.fnAdjustColumnSizing();
  		},
        resize: function() {
            var tid = setInterval(detectInstance, 300);
            function detectInstance() {
                if (mutexTableInstance !== "" && (typeof mutexTableInstance !== "undefined")) {
                    abortTimer();                    
                }
            }
            function abortTimer() { 
                clearInterval(tid);
                mutexTableInstance.fnAdjustColumnSizing();
            }
        }
 	}
 }());