public class PlayerData {
    private static Party playerParty;
    private static PlayerSprite playerSprite;

    public static Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("partyMembers", playerParty.marshal());
        ob.add("playerSprite", playerSprite.marshal());
        return ob;
    }

    public static void unMarshal(Json ob)
    {
        playerParty = new Party(ob.get("partyMembers"));
        playerSprite = new PlayerSprite(ob.get("playerSprite"));
    }

    public static void SetPlayerSprite(PlayerSprite newPlayerSprite)
    {
        playerSprite = newPlayerSprite;
    }

    public static void SetPlayerParty(Party newPlayerParty)
    {
        playerParty = newPlayerParty;
    }

    public static PlayerSprite GetPlayerSprite()
    {
        return playerSprite;
    }

    public static Party GetPlayerParty()
    {
        return playerParty;
    }
}
