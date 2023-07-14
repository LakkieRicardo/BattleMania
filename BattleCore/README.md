# BattleCore

This is the plugin that provides all the basic infrastructure that is common between all BattleMania servers. These include common inventories, commands, listeners, Proxy management, punishment management, etc.

## Inventory API

This core plugin includes an inventory API which currently only contains a "Navigator" inventory type(scrollable inventory). This makes the process of creating Minecraft inventories much simpler and allows for easy management. Each type has its own caveats and will include documentation in the "XXXInvLayout" class where the "XXX" is the type of layout. Here is an example documentation:

```java

/**
 * Represents a type of layout with a list of content items which allows for scrolling through pages. <br/>
 * <br/>
 * In order to use a navigator layout, first define it in the InventoryLayouts.json. There is no special layout type,
 * the only special item type for this layout is NAVIGATOR_CONTENT, NAVIGATOR_PREVIOUS, and NAVIGATOR_NEXT. <br/>
 * <br/>
 * Once done, use the {@link InvLayout#getLayoutJSONFromId(String)} function and pass it to this constructor in order to
 * interpret the JSON at runtime. Then, when you are at the point you need the inventory you can call the
 * {@link #setContentList(List)} and define the inventory's content items. That way, when you call the
 * {@link #updateInventory(Inventory, Object)} function it will dynamically generate the inventory consisting of all
 * the content items you need. <br/>
 * <br/>
 * Once you have the inventory open, you will want to listen to the NavigatorClickEvent in order to check when a player
 * is trying to select one of your content items, if they are trying to navigate, etc. and play sounds, do certain
 * actions, etc. depending on your needs. <br/>
 * <br/>
 * You can also write a function to implement the interface IInvLayoutEffect to add an additional effect onto your
 * inventory. This can be used to render things that may change player-to-player, and is done exclusively after the
 * inventory has already been created.
 */
public class NavigatorInvLayout extends InvLayout {

```