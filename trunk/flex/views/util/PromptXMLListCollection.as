package views.util
{
import mx.collections.XMLListCollection;
import mx.collections.ListCollectionView;
import mx.utils.StringUtil;

public class PromptXMLListCollection extends XMLListCollection
{
	private var prompts:XMLListCollection;
	private var original:XMLListCollection;

	public function PromptXMLListCollection(emptyNode:XML, original:XMLListCollection)
	{
		this.prompts = new XMLListCollection();
		prompts.addItemAt(emptyNode, 0);
		this.original = original;
	}

    override public function getItemAt(index:int, prefetch:int=0):Object
    {
        if (index < 0 || index >= length)
            throw new RangeError("invalid index", index);

		if (index < prompts.length)
			return prompts..getItemAt(index, prefetch);

		return original.getItemAt(index - prompts.length, prefetch);
    }

    override public function get length():int
    {
		return prompts.length + original.length;
    }
}

}