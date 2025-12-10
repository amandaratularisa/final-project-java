package com.game.mario;

import java.util.ArrayList;
import java.util.List;

public class StoryManager {
    
    private List<StorySegment> story;
    private int currentLevel = 1;
    
    public StoryManager() {
        story = new ArrayList<>();
        initializeStory();
    }
    
    private void initializeStory() {
        story.add(new StorySegment(0, 
            "INTRO",
            "Greetings, Lumina Runner! The floating metropolis of Astra Nova is in peril.\nThe Shadow Lord shattered the Prism Core that powers both the Light and Shadow realms!\nSprint across 10 bioluminescent districts to recover every shard and reboot the sky bridges.\nUse ARROW KEYS or WASD to dash, SPACE/W to leap, harvest coins to fuel support drones, and purge corrupted sentries!"));
        
        story.add(new StorySegment(1,
            "AREA 1 - LEVEL 1: Aurora Plaza",
            "You land on the radiant plazas of the Light Realm.\nCrystal walkways pulse with energy while security drones flicker offline.\nStabilize the district and push toward the central transit line!"));
        
        story.add(new StorySegment(2,
            "AREA 1 - LEVEL 2: Prism Causeway",
            "Holographic rails sweep above the clouds, guiding you through neon arches.\nShadow glitches try to corrupt each platform you step on.\nKeep your footing and overcharge the prisms with every coin you grab!"));
        
        story.add(new StorySegment(3,
            "AREA 1 - LEVEL 3: Lumen Aqueduct",
            "The light canals roar beneath transparent bridges.\nMagnetic lifts surge in rhythmic waves—time your jumps or get launched into the mist!\nNeutralize the siphons before they drain the waterways completely."));
        
        story.add(new StorySegment(4,
            "AREA 1 - LEVEL 4: Radiant Bastion",
            "You scale the shield towers that protect the Light Realm's heart.\nSentinel cannons fire corrupted energy bolts from every tier.\nReach the command deck and reroute power to the inter-realm portal!"));
        
        story.add(new StorySegment(5,
            "AREA 1 - LEVEL 5 [BOSS]: LuminCore Spire",
            "At the peak, a colossal Prism Warden blocks the bridge to the Shadow Realm.\nIt bends lasers around the skyline and floods the arena with rotating light walls.\nBreak the warden's mirrors to reclaim the final Light Realm shard!"));
        
        story.add(new StorySegment(6,
            "AREA 2 - LEVEL 6: Umbral Gate",
            "You descend through the portal into the Shadow Realm.\nThe air crackles with void storms and inverted constellations.\nActivate the beacon pylons before the darkness seals your escape route!"));
        
        story.add(new StorySegment(7,
            "AREA 2 - LEVEL 7: Obsidian Railworks",
            "Abandoned sky-trains grind along broken tracks over a bottomless abyss.\nSpectral engineers hurl charged bolts to knock you off balance.\nHijack the rail switches to keep the platforms moving in your favor!"));
        
        story.add(new StorySegment(8,
            "AREA 2 - LEVEL 8: Emberforge Depths",
            "Molten forges spit sparks that paint the cavern walls crimson.\nShadow golems rise from the slag whenever the alarms howl.\nDance between heat vents and send the forge cores into overload!"));
        
        story.add(new StorySegment(9,
            "AREA 2 - LEVEL 9: Voidborne Citadel",
            "Suspended fortresses drift above an endless aurora storm.\nElite phantoms guard prism conduits that feed the Shadow Lord's throne.\nSever every conduit to expose his sanctuary!"));
        
        story.add(new StorySegment(10,
            "AREA 2 - LEVEL 10 [BOSS]: Eclipse Throne",
            "The Shadow Lord manifests amid collapsing constellations!\nHe bends gravity, launches eclipse beams, and cloaks the arena in pitch black.\nStrike when the prism shards flare—only then can you reboot the Prism Core!"));
        
        story.add(new StorySegment(11,
            "VICTORY!",
            "Brilliant work, Runner! The Prism Core is whole again and both realms are back in balance.\nAurora currents sweep across Astra Nova while the Shadow Realm calms to a quiet glow.\nEnjoy the neon skyline you saved—and thank you for keeping the light alive!"));
    }
    
    public StorySegment getStoryForLevel(int level) {
        for (StorySegment segment : story) {
            if (segment.getLevel() == level) {
                return segment;
            }
        }
        return null;
    }
    
    public StorySegment getIntro() {
        return story.get(0);
    }
    
    public StorySegment getVictory() {
        return story.get(story.size() - 1);
    }
    
    public static class StorySegment {
        private int level;
        private String title;
        private String text;
        
        public StorySegment(int level, String title, String text) {
            this.level = level;
            this.title = title;
            this.text = text;
        }
        
        public int getLevel() { return level; }
        public String getTitle() { return title; }
        public String getText() { return text; }
    }
}
