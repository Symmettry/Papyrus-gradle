let x = new AutoSaveValue(15, "x");

x.value += 5;

Server.broadcast(x.value);

Events.onBukkitEvent(Events.MoveEvent, e => {
	/*let a = Item.of("DIAMOND_SWORD");
	a.addAttribute("GENERIC_MAX_HEALTH", 15, "ADD_NUMBER", "legs", "head");
	e.getPlayer().getInventory().addItem(a.getItem());*/
});

Server.registerCommand("test", {
    permission:"op",
}, (p) => {
    p.sendMessage("Hello World!");
})