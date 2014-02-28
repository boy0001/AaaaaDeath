package com.empcraft;
/**
 * 
 */
/**
 * @author Jesse Boyd
 *
 */
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public final class AaaaahDeath extends JavaPlugin implements Listener {
	public static int counter = 0;
	
	final Map<String, Integer> fright = new HashMap<String, Integer>();
	final Map<String, Double> health = new HashMap<String, Double>();
	final Map<String, Float> change = new HashMap<String, Float>();
	
	@EventHandler
	public void dmg(final EntityDamageEvent event) {
		
	if(event.getEntity() instanceof Player) {
	Player player = (Player) event.getEntity();
	try {
	if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.health.enabled")) {
	 if (event.isCancelled()==false) {
		 if (health.get(player.getName())==null) {
			 health.put(player.getName(), player.getHealth());
		 }
	 }
	}
	}
	catch (Exception e) {
		
	}
	}
	}
	@EventHandler
	public void PlayerRespawnEvent(PlayerRespawnEvent event) {
		try {
		Player player = event.getPlayer();
        fright.put(player.getName(),100);
        fright.put(player.getName()+".sleep",100);
        fright.put(player.getName()+".comfort",100);
		}
		catch (Exception e) {
			
		}
	}
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) { 	
		if (event instanceof PlayerDeathEvent) 	{
        Player player = (Player) event.getEntity();
        fright.put(player.getName(),100);
        fright.put(player.getName()+".sleep",100);
        fright.put(player.getName()+".comfort",100);
        if (getConfig().getString("multiworld."+player.getWorld().getName()+".deathban.time").equalsIgnoreCase("false")==false) {
        	String time = Long.toString(timetosec(getConfig().getString("multiworld."+player.getWorld().getName().split("_")[0]+".deathban.time")));
        	getConfig().set("multiworld."+player.getWorld().getName().split("_")[0]+".deathban."+player.getName(), time);
        	for(Player user:getServer().getOnlinePlayers()){
        		user.sendMessage(ChatColor.BLUE+player.getName()+" "+getmsg("MSG5")+" "+ChatColor.BLUE+player.getWorld().getName()+ChatColor.GRAY+" - "+ChatColor.RED+getmsg("MSG6")+" "+(getConfig().getString("multiworld."+player.getWorld().getName().split("_")[0]+".deathban.time")));
        	}
        	saveConfig();
        	reloadConfig();
        	//TODO  save
        	
        	
        	
        	
        }
	}
	}
	@EventHandler
	void PlayerTeleportEvent(PlayerTeleportEvent event){ 
		Player player = event.getPlayer();
		if (getConfig().getString("multiworld."+event.getTo().getWorld().getName().split("_")[0]+".enabled").equalsIgnoreCase("true")) {
			try {
				if (Long.parseLong((getConfig().getString("multiworld."+event.getTo().getWorld().getName().split("_")[0]+".deathban."+player.getName())))<(System.currentTimeMillis()/1000)) {
					getConfig().getConfigurationSection(getConfig().getString("multiworld."+event.getTo().getWorld().getName().split("_")[0]+".deathban")).set(player.getName(), null);
				}
				else {
					msg(player,ChatColor.GRAY+getmsg("MSG7")+" "+ChatColor.RED+((Long.parseLong((getConfig().getString("multiworld."+event.getTo().getWorld().getName().split("_")[0]+".deathban."+player.getName())))-(System.currentTimeMillis()/1000))/60)+ChatColor.GRAY+" minutes.");
					event.setCancelled(true);
				}
			}
			catch (Exception e) {
			}
		}
	}
	
	
	
	@EventHandler
	public void PlayerBedLeave(PlayerBedLeaveEvent event){
		if (getConfig().getString("multiworld."+event.getPlayer().getWorld().getName()+".enabled").equalsIgnoreCase("true")) {
			if (event.getPlayer().getWorld().getTime()>=0) {
				fright.put(event.getPlayer().getName()+".sleep",100);
			}
		}
	}
	
	public Long timetosec(String input) {
    	try {
    	input = input.toLowerCase().trim();
    	int toadd = 0;
    	if (input.contains("w")) {
    		toadd = 604800*Integer.parseInt(input.split("w")[0].trim());
    	}
    	else if ((input.contains("d"))&&(input.contains("sec")==false)) {
    		toadd = 86400*Integer.parseInt(input.split("d")[0].trim());
    	}
    	else if (input.contains("h")) {
    		toadd = 3600*Integer.parseInt(input.split("h")[0].trim());
    	}
    	else if (input.contains("m")) {
    		toadd = 60*Integer.parseInt(input.split("m")[0].trim());
    	}
    	else if (input.contains("s")) {
    		toadd = Integer.parseInt(input.split("s")[0].trim());
    	}
    	
    	Long time = (System.currentTimeMillis()/1000)+toadd;
    	if (toadd!=0) {
		return time;
    	}
    	else {
    		return null;
    	}
    	}
    	catch (Exception e) {
    		return null;
    	}
    }
	public boolean contains(String[] list, String check) {
		for (String current: list) {
			if (current.equals(check)) {
				return true;
			}
		}
		return false;
	}
    public void msg(Player player,String mystring) {
    	if (mystring==null||mystring.equals("")) {
    		return;
    	}
    	if (player==null) {
    		getServer().getConsoleSender().sendMessage(colorise(mystring));
    	}
    	else if (player instanceof Player==false) {
    		getServer().getConsoleSender().sendMessage(colorise(mystring));
    	}
    	else {
    		player.sendMessage(colorise(mystring));
    	}

    }
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		else {
			player = null;
		}
    	String line = "";
    	for (String i:args) {
    		line+=i+" ";
    	}
    	line = line.trim();
		if((cmd.getName().equalsIgnoreCase("ad"))||(cmd.getName().equalsIgnoreCase("aaaaahdeath"))||(cmd.getName().equalsIgnoreCase("death"))){
			boolean hasperm = true;
			boolean worked = false;
			if (args.length > 0) {
				if ((args[0].equalsIgnoreCase("reload"))){
	    			hasperm = false;
	    			if (sender instanceof Player==false) {
	    				hasperm = true;
	    				msg(player,getmsg("MSG8"));
	    			}
	    			else if (((Player) sender).hasPermission("ad.reload")) {
	    				hasperm = true;
	    			}
	    			
	    			if (hasperm) { 
	    				worked = true;
	    		        for (Player user:getServer().getOnlinePlayers()) {
	    		        	fright.put(user.getName(),100);
	    		            try {
	    		            	Set<String> vars = getConfig().getConfigurationSection("fright.users."+user.getName()).getKeys(false);
	    		            	for(String current : vars) {
	    		            		System.out.println(current+" | "+this.getConfig().getInt("fright.users."+user.getName()+"."+current));
	    		        			fright.put(user.getName()+"."+current, this.getConfig().getInt("fright.users."+user.getName()+"."+current));
	    		        		}
	    		            }
	    		            catch (Exception e) {
	    		            	
	    		            }
	    		        }
	        			this.reloadConfig();
	        			this.saveDefaultConfig();
	    			}
				}
				else if ((args[0].equalsIgnoreCase("info"))){
					msg(player,ChatColor.LIGHT_PURPLE+getmsg("MSG9"));
					return true;
				}
				else if ((args[0].equalsIgnoreCase("help"))){
					worked = true;
					// list of commands
					msg(player,ChatColor.GOLD+"====HELP====");
					msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD help "+ChatColor.GRAY+"- shows this page.");
					if (checkperm(player,"ad.reload")==true) { 
						msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD reload "+ChatColor.GRAY+"- reloads the config.");
					}
					if (player==null) {
						msg(player,ChatColor.RED+getmsg("MSG25")+".");
						return false;
					}
					if (getConfig().getString("multiworld."+player.getWorld().getName()+".enabled").equalsIgnoreCase("true")) {
						msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD info "+ChatColor.GRAY+getmsg("MSG10")+ChatColor.GRAY+".");
						msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD "+getmsg("MSG0")+" "+ChatColor.GRAY+getmsg("MSG11")+ChatColor.GRAY+".");
						msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD "+getmsg("MSG1")+" "+ChatColor.GRAY+getmsg("MSG12")+ChatColor.GRAY+".");
						msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD "+getmsg("MSG2")+" "+ChatColor.GRAY+getmsg("MSG13")+ChatColor.GRAY+".");
						msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD "+getmsg("MSG3")+" "+ChatColor.GRAY+getmsg("MSG14")+ChatColor.GRAY+".");
						msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD "+getmsg("MSG4")+" "+ChatColor.GRAY+"- "+getmsg("MSG15")+ChatColor.GRAY+".");
					}
					else {
						msg(player,ChatColor.RED+getmsg("MSG25")+".");
					}
				}
				else if ((args[0].equalsIgnoreCase(getmsg("MSG4")))){
					worked = true;
					msg(player,getmsg("MSG16").replace("//","\n"));
				}
				else if ((args[0].equalsIgnoreCase(getmsg("MSG1")))){
					worked = true;
					msg(player,getmsg("MSG17").replace("//","\n"));
				}
				else if ((args[0].equalsIgnoreCase(getmsg("MSG0")))){
					worked = true;
					msg(player,getmsg("MSG18").replace("//","\n"));
				}
				else if ((args[0].equalsIgnoreCase(getmsg("MSG2")))){
					worked = true;
					msg(player,getmsg("MSG19").replace("//","\n"));
				}
				else if ((args[0].equalsIgnoreCase(getmsg("MSG3")))){
					worked = true;
					msg(player,getmsg("MSG20").replace("//","\n"));
				}
			}
			if ((hasperm==true)&&(worked==false)) {
				msg(player,ChatColor.GRAY+getmsg("MSG21")+": "+ChatColor.RED+"`"+line+"'"+ChatColor.GRAY+". "+ChatColor.BLUE+getmsg("MSG22")+":");
				msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD help");
				msg(player,ChatColor.GRAY+" - "+ChatColor.GREEN+"/AD info");
				msg(player,ChatColor.GRAY+" - "+ChatColor.RED+"/suicide");
			}
			else if (hasperm == false){
				msg(player,ChatColor.GRAY+"You do not have permission for "+ChatColor.RED+"/AD "+line+ChatColor.GRAY+".");
				msg(player,ChatColor.BLUE+getmsg("MSG22")+":"+ChatColor.GRAY+"\n - /AD help");
			}
		}
		return true;
	}
    
	
    Timer timer = new Timer ();
	
	TimerTask mytask = new TimerTask () {
		@Override
	    public void run () {
			counter++;
			if (health.size()>0) {
				Iterator<String> iterator = health.keySet().iterator();
				String pname = iterator.next();
				if (Bukkit.getPlayer(pname)!=null) {
					Player player = Bukkit.getPlayer(pname);
					Random random = new Random();
					if ((health.get(pname)-player.getHealth())>0) {
						fright.put(player.getName(),fright.get(player.getName())-random.nextInt((int) ((health.get(pname)-player.getHealth())*10)));
					}
					else {
					}
					if (fright.get(player.getName()) < 0) {
						fright.put(player.getName(),0);
					}
					health.remove(pname);
				}
			}
			if (counter%600==0) {
				System.out.println(getmsg("MSG24S"));
				getConfig().getConfigurationSection("fright").set("users", null);
		        for (final Entry<String, Integer> node : fright.entrySet()) {
		        	getConfig().options().copyDefaults(true);
		        	getConfig().set("fright.users."+(""+node.getKey()), (node.getValue()));
		        	
		        	saveConfig();
		        }
		        System.out.println("DONE!");
			}
			else if (counter%1==0) {
				for (Player player:getServer().getOnlinePlayers()) {
					ScoreboardManager manager = Bukkit.getScoreboardManager();
					Scoreboard sidebar = manager.getNewScoreboard();
					Objective objective = sidebar.registerNewObjective("test", "dummy");
					objective.setDisplayName("Stats:");
//					CommonScoreboard  board = CommonScoreboard.get(player);
//					CommonObjective sidebar = board.getObjective(Display.SIDEBAR);
//					sidebar.setDisplayName("Stats:");
					if (getConfig().getString("multiworld."+player.getWorld().getName()+".enabled").equalsIgnoreCase("true")) {
						if (getConfig().getString("multiworld."+player.getWorld().getName()+".gui").equalsIgnoreCase("true")) {
							objective.setDisplaySlot(DisplaySlot.SIDEBAR);
						}
						else {
							player.setScoreboard(manager.getNewScoreboard());
						}
	    				int total = 0;
	    				int Average = 0;
						if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.comfort.enabled")) {
							int comfort = 100;
							Block block = player.getLocation().getBlock();
							if (block.getLightLevel()<13) {
								comfort-=Math.round(Math.sqrt(192*(13-block.getLightLevel())));
							}
							String blockid = ""+block.getTypeId();
							Location myloc = new Location(player.getWorld(),player.getLocation().getX(),player.getLocation().getY()-1,player.getLocation().getZ());
							String myblock = "" + myloc.getBlock().getTypeId();
							Location myloc2 = new Location(player.getWorld(),player.getLocation().getX(),player.getLocation().getY()+2,player.getLocation().getZ());
							int above = myloc2.getBlock().getTypeId();
							String[] beautiful = getConfig().getString("multiworld."+player.getWorld().getName()+".stats.comfort.beautiful").split(",");
							String[] nice = getConfig().getString("multiworld."+player.getWorld().getName()+".stats.comfort.nice").split(",");
							String[] ugly = getConfig().getString("multiworld."+player.getWorld().getName()+".stats.comfort.ugly").split(",");
							if (contains(beautiful,myblock)||contains(beautiful,blockid)) {
								comfort+=25;
							}
							else if (contains(nice,myblock)||contains(nice,blockid)) {
								comfort-=0;
							}
							else if (contains(ugly,myblock)||contains(ugly,blockid)) {
								comfort-=50;
							}
							else {
								comfort-=25;
							}
							if (above != 0) {
								comfort-=25;
							}
							if (player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
								comfort-=50;
							}
							else if (player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
								comfort-=50;
							}
							if (comfort > 100) {
								comfort = 100;
							}
							else if (comfort < 0) {
								comfort = 0;
							}
							fright.put(player.getName()+".comfort", comfort);
						}
						if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.happiness.enabled")) {
							if (fright.get(player.getName())<1) {
								fright.put(player.getName(), 0);
							}
							if (fright.get(player.getName())<50) {
			    				
			    				Random random = new Random();
			    				if (true) {
			    					int mynum = random.nextInt(1500/(1+fright.get(player.getName())));
				    				if (random.nextInt((1+fright.get(player.getName())))<5) {
				    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW_DIGGING, 8*mynum, 1);
				    					potionEffect.apply(player);
				    				}
				    				else if (random.nextInt((1+fright.get(player.getName())))<5) {
				    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.WEAKNESS, 6*mynum, 1);
				    					potionEffect.apply(player);
				    				}
				    				else if (random.nextInt((1+fright.get(player.getName())))<5) {
				    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 4*mynum, 1);
				    					potionEffect.apply(player);
				    				}
				    				else if (random.nextInt((1+fright.get(player.getName())))<5) {
				    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.CONFUSION, 4*mynum, 1);
				    					potionEffect.apply(player);
				    				}
				    				else if (random.nextInt((1+fright.get(player.getName())))<5) {
				    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 4*mynum, 1);
				    					potionEffect.apply(player);
				    				}
				    				else if (random.nextInt(2*(1+fright.get(player.getName())))<5) {
				    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.HUNGER, 4*mynum, 1);
				    					potionEffect.apply(player);
				    				}
			    				}
		    				if (fright.get(player.getName())<15) {
			    				if (random.nextInt(3*(1+fright.get(player.getName())))==1) {
			    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 100, 10);
			    					potionEffect.apply(player);
			    				}
			    				else if (random.nextInt(3*(1+fright.get(player.getName())))==1) {
			    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.HUNGER, 100, 10);
			    					potionEffect.apply(player);
			    				}
			    				else if (random.nextInt(5*(1+fright.get(player.getName())))==1) {
			    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.POISON, 40, 1);
			    					potionEffect.apply(player);
			    				}
			    				else if (fright.get(player.getName())<5) {
			    					 if (random.nextInt(8*(1+fright.get(player.getName())))==1) {
					    					PotionEffect potionEffect = new PotionEffect(PotionEffectType.HARM, 1, 0);
					    					potionEffect.apply(player);
					    				}
				    	    		if (fright.get(player.getName())<1) {
				    	    			fright.put(player.getName(), 0);
					    				if (random.nextInt(5*(1+fright.get(player.getName())))==1) {
						    				try {
						    					player.setHealth(0);
						    				}
						    				catch (Exception e2) {
						    					try {
						    					fright.put(player.getName(),100);
						    					}
						    					catch (Exception e) {
						    						
						    					}
						    				}
						    				}
				    	    		}

			    				}
			    				
			    				}
			    	    		

			    			}
						}
		    			if (true) {

		    				Random random = new Random();
		    				if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.sleep.enabled")) {
		    					try {
		    						if (fright.get(player.getName()+".sleep")> 0) {
		    							if (random.nextInt(12)==0) {
		    								fright.put(player.getName()+".sleep",fright.get(player.getName()+".sleep")-1);
		    								if (random.nextInt(12)==0) {
		    									if (player.getFoodLevel()>1) {
		    										player.setFoodLevel(player.getFoodLevel()-1);
		    									}
		    								}
		    							}
		    						}
		    					}
		    					catch (Exception e3) {
		    						fright.put(player.getName()+".sleep",100);
		    					}
		    				}
		    					
		    					if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.hunger.enabled")) {
		    						fright.put(player.getName()+".hunger",player.getFoodLevel()*5);
		    						Average += fright.get(player.getName()+".hunger");
		    						total +=1;
		    					if (fright.get(player.getName()+".hunger")<15) {
		    						total+=1;
		    						fright.put(player.getName(),fright.get(player.getName())-random.nextInt(1));
		    						if (fright.get(player.getName()+".hunger")<10) {
		    							total+=2;
		    							fright.put(player.getName(),fright.get(player.getName())-random.nextInt(2));
		    							if (fright.get(player.getName()+".hunger")<2) {
		    								total+=3;
		    								fright.put(player.getName(),fright.get(player.getName())-random.nextInt(6));
		    							}
		    						}
		    					}
		    					}
		    					if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.sleep.enabled")) {
		    					if (fright.get(player.getName()+".sleep")<15) {
		    						total+=1;
		    						fright.put(player.getName(),fright.get(player.getName())-random.nextInt(1));
		    						if (fright.get(player.getName()+".sleep")<10) {
		    							total+=2;
		    							fright.put(player.getName(),fright.get(player.getName())-random.nextInt(2));
		    							if (fright.get(player.getName()+".sleep")<2) {
		    								total+=3;
		    								fright.put(player.getName(),fright.get(player.getName())-random.nextInt(3));
		    							}
		    						}
		    					}
		    					if (player.isSleeping()) {
		    						fright.put(player.getName()+".sleep",fright.get(player.getName()+".sleep")+random.nextInt(4));
		    					}
		    					Average+=fright.get(player.getName()+".sleep");
		    					total +=1;
		    					}
		    					if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.comfort.enabled")) {
		    					if (fright.get(player.getName()+".comfort")<15) {
		    						total+=1;
		    						if (fright.get(player.getName()+".comfort")<10) {
		    							total+=2;
		    							if (fright.get(player.getName()+".comfort")<2) {
		    								total+=3;
		    								fright.put(player.getName(),fright.get(player.getName())-random.nextInt(1));
		    							}
		    						}
		    					}
		    					Average+=fright.get(player.getName()+".comfort");
		    					total +=1;
		    					}
	    					if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.health.enabled")) {
		    					fright.put(player.getName()+".health",(int) Math.round(player.getHealth()*5));
		    					Average += fright.get(player.getName()+".health");
		    					total +=1;
		    					if (fright.get(player.getName()+".health")<15) {
		    						total+=1;
		    						fright.put(player.getName(),fright.get(player.getName())-random.nextInt(1));
		    						if (fright.get(player.getName()+".health")<10) {
		    							total+=2;
		    							fright.put(player.getName(),fright.get(player.getName())-random.nextInt(2));
		    							if (fright.get(player.getName()+".health")<2) {
		    								total+=3;
		    								fright.put(player.getName(),fright.get(player.getName())-random.nextInt(3));
		    							}
		    						}
		    					}
	    					}	
	    						try {
		    					Average = (Average)/(total);
	    						}
	    						catch (Exception e) {
	    							
	    						}
		    					int num = (Math.abs(Average-fright.get(player.getName()))/10)+1;
		    					if (Average < fright.get(player.getName())) {
		    						
		    						if (fright.get(player.getName())>0) {
		    							fright.put(player.getName(), fright.get(player.getName())-(random.nextInt(num)));
		    						}
		    					}
		    					else {
		    						fright.put(player.getName(), fright.get(player.getName())+random.nextInt(num));
		    					}
		    			}
						try {
							if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.happiness.enabled")) {
								Score myscore = objective.getScore(Bukkit.getOfflinePlayer(getmsg("MSG0")));
								myscore.setScore((int) fright.get(player.getName()));
								
							}
							if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.health.enabled")) {
								Score myscore = objective.getScore(Bukkit.getOfflinePlayer(getmsg("MSG1")));
								myscore.setScore((int) fright.get(player.getName()+".health"));
								
								
							}
							if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.hunger.enabled")) {
								Score myscore = objective.getScore(Bukkit.getOfflinePlayer(getmsg("MSG2")));
								myscore.setScore((int) fright.get(player.getName()+".hunger"));
								
							}
							if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.sleep.enabled")) {
								Score myscore = objective.getScore(Bukkit.getOfflinePlayer(getmsg("MSG3")));
								myscore.setScore((int) fright.get(player.getName()+".sleep"));
							}
							if (getConfig().getBoolean("multiworld."+player.getWorld().getName()+".stats.comfort.enabled")) {
								Score myscore = objective.getScore(Bukkit.getOfflinePlayer(getmsg("MSG4")));
								myscore.setScore((int) fright.get(player.getName()+".comfort"));
							}
							
							
							
							player.setScoreboard(sidebar);
//							CommonScore mygui = sidebar.getScore(player.getName());
//							mygui.update();
						}
						catch (Exception e) {
							System.out.println("ERROR "+e);
							
							
//							CommonScore mygui = sidebar.getScore(player.getName());
//							mygui.setValue((int) fright.get(player.getName()));
//							mygui.update();
						}
					}
					else {
						player.setScoreboard(manager.getNewScoreboard());
					}
		  		}
			}
		}
	};
	
	public String getmsg(String key) {
		File yamlFile = new File(getDataFolder(), getConfig().getString("language").toLowerCase()+".yml"); 
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(yamlFile);
		try {
			return colorise(YamlConfiguration.loadConfiguration(yamlFile).getString(key));
		}
		catch (Exception e){
			return "";
		}
	}
	
	@Override
    public void onEnable(){
		
		saveResource("english.yml", true);
		saveResource("french.yml", true);
        getConfig().options().copyDefaults(true);
        final Map<String, Object> options = new HashMap<String, Object>();
        getConfig().set("version", "0.0.1");
        options.put("language","english");
        options.put("multiworld.world",false);
        for(World world : getServer().getWorlds()) {
        	options.put("multiworld."+world.getName()+".enabled",true);
        	options.put("multiworld."+world.getName()+".deathban.time","false");
        	options.put("multiworld."+world.getName()+".gui",true);
        	options.put("multiworld."+world.getName()+".stats.happiness.enabled",true);
        	options.put("multiworld."+world.getName()+".stats.health.enabled",true);
        	options.put("multiworld."+world.getName()+".stats.hunger.enabled",true);
        	options.put("multiworld."+world.getName()+".stats.sleep.enabled",true);
        	options.put("multiworld."+world.getName()+".stats.comfort.enabled",true);
        	List<Integer> beautiful = Arrays.asList(171,35,45);
        	List<Integer> nice = Arrays.asList(5,155,17);
        	List<Integer> ugly = Arrays.asList(110,3,19,7,4,13,17);
        	options.put("multiworld."+world.getName()+".stats.comfort.beautiful","171,35,45");
        	options.put("multiworld."+world.getName()+".stats.comfort.nice","5,155,17");
        	options.put("multiworld."+world.getName()+".stats.comfort.ugly","110,3,19,7,4,13,17");
        }
        for (final Entry<String, Object> node : options.entrySet()) {
          	 if (!getConfig().contains(node.getKey())) {
          		 getConfig().set(node.getKey(), node.getValue());
          	 }
          }
        for (Player player:getServer().getOnlinePlayers()) {
        	fright.put(player.getName(),100);
            try {
            	Set<String> vars = getConfig().getConfigurationSection("fright.users."+player.getName()).getKeys(false);
            	for(String current : vars) {
        			fright.put(player.getName()+"."+current, this.getConfig().getInt("fright.users."+player.getName()+"."+current));
        		}
            }
            catch (Exception e) {
            	
            }
        }
    	saveConfig();
    	this.saveDefaultConfig();
    	getServer().getPluginManager().registerEvents(this, this);
    	timer.schedule (mytask,0l, 1000);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	try {
    		if (fright.get("fright.users."+event.getPlayer().getName())!=null) {
    			
    		}
    		else {
    			fright.put(event.getPlayer().getName(),100);
    		}
    	}
    	catch (Exception e) {
    		fright.put(event.getPlayer().getName(),100);
    	}
    }
    @Override
    public void onDisable() {
    	System.out.println("[Aaaaa!Death] Scaring players...");
    	try {
		getConfig().getConfigurationSection("fright").set("users", null);
    	}
    	catch (Exception e) {
    		
    	}
        for (final Entry<String, Integer> node : fright.entrySet()) {
        	getConfig().options().copyDefaults(true);
        	getConfig().set("fright.users."+(""+node.getKey()), (node.getValue()));
        	
        	saveConfig();
        }
        System.out.println("DONE!");
    	try {
    	timer.cancel();
    	timer.purge();
    	}
    	catch (IllegalStateException e) {
    		
    	}
    	catch (Throwable e) {
    		
    	}
    	this.reloadConfig();
    	this.saveConfig();
    }

	
	
	
	
	
	
	
    public String colorise(String mystring) {
    	String[] codes = {"&1","&2","&3","&4","&5","&6","&7","&8","&9","&0","&a","&b","&c","&d","&e","&f","&r","&l","&m","&n","&o","&k"};
    	for (String code:codes) {
    		mystring = mystring.replace(code, "§"+code.charAt(1));
    	}
    	return mystring;
    }
    public boolean checkperm(Player player,String perm) {
    	boolean hasperm = false;
    	String[] nodes = perm.split("\\.");
    	String n2 = "";
    	if (player==null) {
    		return true;
    	}
    	else if (player.hasPermission(perm)) {
    		hasperm = true;
    	}
    	else if (player.isOp()==true) {
    		hasperm = true;
    	}
    	else {
    		for(int i = 0; i < nodes.length-1; i++) {
    			n2+=nodes[i]+".";
            	if (player.hasPermission(n2+"*")) {
            		hasperm = true;
            	}
    		}
    	}
		return hasperm;
    }
}