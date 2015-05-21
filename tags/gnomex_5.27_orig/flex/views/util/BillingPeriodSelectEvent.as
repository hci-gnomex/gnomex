package views.util
{
	import flash.events.Event;
	
	public class BillingPeriodSelectEvent extends Event
	{

		public var selectedMonthYear:String = null;
		
		public function BillingPeriodSelectEvent(selMonthYear:String)
		{
			selectedMonthYear = selMonthYear;
			super("BillingPeriodSelectEvent", true, false);
		}
	
	}
}