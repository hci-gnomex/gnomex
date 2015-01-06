package views.util
{
	import hci.flex.util.DictionaryManager;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.collections.XMLListCollection;

	public class BillingPeriodUtil
	{
		private var _billingPeriods:XMLListCollection;
		private var _activeBillingPeriods:ArrayCollection;
		private var _minBillingYear:int;
		private var _maxBillingYear:int;
		private var months:Array = new Array(12);
		
		public function BillingPeriodUtil(dictionaryManager:DictionaryManager)
		{
			// Get sorted billing periods
			_billingPeriods = new XMLListCollection(dictionaryManager.getEntries("hci.gnomex.model.BillingPeriod"));
			var billingPeriodSort:Sort = new Sort();
			billingPeriodSort.compareFunction = this.sortBillingPeriods;	   
			_billingPeriods.sort = billingPeriodSort;  
			_billingPeriods.refresh();
		
			// get array of active billing period display strings.
			_activeBillingPeriods = new ArrayCollection();
			for each(var de:Object in _billingPeriods) {
				var currDisplay:String = de.@display;
				if (currDisplay.length > 3) {
					var currentYear:int = parseInt(de.@calendarYear);
					if(currentYear < minBillingYear) {
						_minBillingYear = currentYear;
					}
					if(currentYear > maxBillingYear) {
						_maxBillingYear = currentYear;
					}
					_activeBillingPeriods.addItem(currDisplay);
				}
			} 
			
			// Populate months array
			months[0] = "Jan";
			months[1] = "Feb";
			months[2] = "Mar";
			months[3] = "Apr";
			months[4] = "May";
			months[5] = "Jun";
			months[6] = "Jul";
			months[7] = "Aug";
			months[8] = "Sep";
			months[9] = "Oct";
			months[10] = "Nov";
			months[11] = "Dec";	
		}
		
		public function get billingPeriods():XMLListCollection {
			return _billingPeriods;
		}
		
		public function get activeBillingPeriods():ArrayCollection {
			return _activeBillingPeriods;
		}
		
		public function get minBillingYear():int {
			return _minBillingYear;
		}
		
		public function get maxBillingYear():int {
			return _maxBillingYear;
		}
		
		public function getBillingPeriodFromYearMonth(yearMonth:String):Object {
			var bp:Object = null;
			for each(var de:Object in _billingPeriods) {
				if (de.@display == yearMonth) {
					bp=de;
				}
			}
			return bp;
		}
		
		public function getPreviousBillingPeriod():Object {
			var nowYear:int = (new Date()).getFullYear();
			var nowMonth:int = (new Date()).getMonth();
			return getPreviousBillingPeriodFromYearMonth(buildYearMonth(nowYear, nowMonth));
		}
		
		private function buildYearMonth(year:int, month:int):String {
			return months[month] + " " + year.toString()
		}
		
		public function getPreviousBillingPeriodFromYearMonth(yearMonth:String):Object {
			var year:int = parseInt(yearMonth.substr(yearMonth.length - 4));
			var monthStr:String = yearMonth.substr(0,3);
			var month:int = 0;
			for(var i:int = 0; i < 12; i++) {
				if (months[i] == monthStr) {
					month = i;
					break;
				}
			}
			if (month == 0) {
				month = 11;
				year--;
			} else {
				month--;
			}
			yearMonth = buildYearMonth(year, month);
			return getBillingPeriodFromYearMonth(yearMonth);
		}
		
		private function sortBillingPeriods(obj1:Object, obj2:Object, fields:Array=null):int {
			if (obj1 == null && obj2 == null) {
				return 0;
			} else if (obj1 == null) {
				return 1;
			} else if (obj2 == null) {
				return -1;
			} else {
				var order1:Object = obj1.@startDateSort;
				var order2:Object = obj2.@startDateSort;
				
				if (obj1.@value == '') {
					return -1;
				} else if (obj2.@value == '') {
					return 1;
				} else {
					if (order1 < order2) {
						return -1;
					} else if (order1 > order2) {
						return 1;
					} else {
						return 0;
					}
				}
			}			
		}			 
	}
}