package ext.com.vos
{
	public class Person
	{
		public static const FIELD_FIRST_NAME:String = "firstName";
		public static const FIELD_LAST_NAME:String 	= "lastName";
		public static const FIELD_EMAIL:String	 	= "email";
		
		public var firstName:String;
		public var lastName:String;
		public var email:String;
		
		public function Person( firstName:String = null, lastName:String = null, email:String = null ):void
		{
			this.firstName 	= firstName;
			this.lastName	= lastName;
			this.email		= email;
		}
	}
}