package views.util
{
    /**                                                                                                                                                                 
     *  @private                                                                                                                                                        
     */
    public class FlexAppVersion
    {
	public function getFlexAppVersion():String	{
		return "5.37.0";
	}
	
	public function getFlexBuildDate():String {
		return "Dec-16-2016 13:12";
    }

	public function getFlexVersionString():String {
	    return getFlexAppVersion() + " (" + getFlexBuildDate() + ")";
	}
	
	}
}
