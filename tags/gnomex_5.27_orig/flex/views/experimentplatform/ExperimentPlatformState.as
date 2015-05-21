package views.experimentplatform {
	import hci.flex.util.DictionaryManager;
	
	import mx.collections.XMLListCollection;
	
	import views.util.DirtyNote;
	
	public class ExperimentPlatformState {
		private var _requestCategory:Object = null;
		private var _dirty:DirtyNote = null;
		private var _dictionaryManager:DictionaryManager = null;
		private var _selectedType:Object = null;
		
		private var xml:XML = 
			<data>
			  <RunMode value="Y" display="Rapid Run Mode"/>
			  <RunMode value="N" display="High Output Run Mode"/>
			</data>;
		private var _runModeOptions:XMLList = XMLList(xml..RunMode);

		public function ExperimentPlatformState(requestCategory:Object, dirty:DirtyNote, dictionaryManager:DictionaryManager) {
			this._requestCategory = requestCategory;
			this._dirty = dirty;
			this._dictionaryManager = dictionaryManager;
			if (_requestCategory != null && _dictionaryManager != null) {
				_selectedType = _dictionaryManager.getEntry("hci.gnomex.model.RequestCategoryType", _requestCategory.@type);
			} else {
				// should never happen
				_selectedType = null;
			}
		}
		
		public function get requestCategory():Object {
			return _requestCategory;
		}
		
		public function get dirty():DirtyNote {
			return _dirty;
		}
		
		public function get dictionaryManager():DictionaryManager {
			return _dictionaryManager;
		}
		
		public function get selectedType():Object {
			return _selectedType;
		}
		public function set selectedType(selectedType:Object):void {
			_selectedType = selectedType;
		}
		
		public function get isIllumina():Boolean {
			if (_selectedType != null && _selectedType.@isIllumina == 'Y') {
				return true;
			} else {
				return false;
			}
		}
		public function get isHiSeq():Boolean {
			if (_selectedType != null && _selectedType.@value == 'HISEQ') {
				return true;
			} else {
				return false;
			}
		}
		public function get isMicroarray():Boolean {
			return _selectedType != null && _selectedType.@value == 'MICROARRAY';
		}
		
		public function get isSequenom():Boolean {
			return _selectedType != null && _selectedType.@value == 'SEQUENOM';
		}
		
		public function get isNanoString():Boolean {
			return _selectedType != null && _selectedType.@value == 'NANOSTRING';
		}
		
		public function get isQC():Boolean {
			return _selectedType != null && selectedType.@value == 'QC';
		}
		
		public function get runModeOptions():XMLList {
			return _runModeOptions;
		}
	}
}