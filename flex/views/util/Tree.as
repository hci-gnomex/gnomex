package views.util
{
	import mx.controls.Tree;

	public class Tree extends mx.controls.Tree
	{
		override public function get verticalScrollPosition():Number
		{
			return Math.max(0, super.verticalScrollPosition);
		}
	}
}