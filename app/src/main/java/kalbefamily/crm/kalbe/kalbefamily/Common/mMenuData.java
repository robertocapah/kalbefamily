package kalbefamily.crm.kalbe.kalbefamily.Common;

public class mMenuData {
	public synchronized String get_IntMenuID() {
		return _IntMenuID;
	}
	public synchronized void set_IntMenuID(String _IntMenuID) {
		this._IntMenuID = _IntMenuID;
	}
	public synchronized String get_intVisible() {
		return _intVisible;
	}
	public synchronized void set_intVisible(String _intVisible) {
		this._intVisible = _intVisible;
	}
	public synchronized Long get_intId() {
		return _intId;
	}
	public synchronized void set_intId(Long _intId) {
		this._intId = _intId;
	}
	public synchronized Long get_IntOrder() {
		return _IntOrder;
	}
	public synchronized void set_IntOrder(Long _IntOrder) {
		this._IntOrder = _IntOrder;
	}
	public synchronized Long get_IntParentID() {
		return _IntParentID;
	}
	public synchronized void set_IntParentID(Long _IntParentID) {
		this._IntParentID = _IntParentID;
	}
	public synchronized String get_TxtDescription() {
		return _TxtDescription;
	}
	public synchronized void set_TxtDescription(String _TxtDescription) {
		this._TxtDescription = _TxtDescription;
	}
	public synchronized String get_TxtLink() {
		return _TxtLink;
	}
	public synchronized void set_TxtLink(String _TxtLink) {
		this._TxtLink = _TxtLink;
	}
	public synchronized String get_TxtMenuName() {
		return _TxtMenuName;
	}
	public synchronized void set_TxtMenuName(String _TxtMenuName) {
		this._TxtMenuName = _TxtMenuName;
	}
	private Long _intId;
	public mMenuData(Long _intId, Long _IntOrder, Long _IntParentID,
					 String _TxtDescription, String _TxtLink, String _TxtMenuName, String _intVisible, String _IntMenuID) {
		this._intId = _intId;
		this._IntOrder = _IntOrder;
		this._IntParentID = _IntParentID;
		this._TxtDescription = _TxtDescription;
		this._TxtLink = _TxtLink;
		this._TxtMenuName = _TxtMenuName;
		this._intVisible = _intVisible;
		this._IntMenuID=_IntMenuID;
	}

	public mMenuData() {
		super();
		// TODO Auto-generated constructor stub
	}
	private Long _IntOrder;
	private Long _IntParentID;
	private String _TxtDescription;
	private String _TxtLink;
	private String _TxtMenuName;
	private String _intVisible;
	private String _IntMenuID;
	private String _TxtIcon;
	
	public String Property_intVisible="intVisible";
	public String Property_intId="intId";
	public String Property_API_IntMenuID="IntMenuID";
	public String Property_IntOrder="IntOrder";
	public String Property_IntParentID="IntParentID";
	public String Property_TxtDescription="TxtDescription";
	public String Property_TxtLink="TxtLink";
	public String Property_TxtMenuName="TxtMenuName";
	public String Property_ListOfMMenuData="ListOfMMenuData";
	public String Property_TxtIcon="TxtIcon";

	public String Property_All=Property_intId +","+
			Property_IntOrder +","+
			Property_IntParentID +","+
			Property_TxtDescription +","+
			Property_TxtLink +","+
			Property_TxtMenuName +","+
			Property_intVisible +","+
			Property_TxtIcon + "," +
			Property_API_IntMenuID
			;

	public String get_TxtIcon() {
		return _TxtIcon;
	}

	public void set_TxtIcon(String _TxtIcon) {
		this._TxtIcon = _TxtIcon;
	}
}
