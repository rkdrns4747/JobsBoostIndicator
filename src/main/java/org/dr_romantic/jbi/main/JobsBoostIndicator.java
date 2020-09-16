package org.dr_romantic.jbi.main;


import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.Permission;
import me.TechsCode.UltraPermissions.storage.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.awt.Color;
import java.util.List;

public class JobsBoostIndicator extends JavaPlugin {
    public boolean availability = true;
    private final String prefix = net.md_5.bungee.api.ChatColor.of(new Color(0, 128, 255)) + "[ "+ChatColor.GREEN + "지구렐름 "+net.md_5.bungee.api.ChatColor.of(new Color(0, 128, 255)) + "]";
    public static void main(String[] args){

    }

    public void onEnable(){
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"[WARNING] This plugin has been made only for EarthRealm.");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"Since all compatibilities are focused on EarthRealm's system, this plugin can make an "+ChatColor.RED+"ERROR" + ChatColor.YELLOW + " on another server.");
        if(!Bukkit.getPluginManager().isPluginEnabled("UltraPermissions")){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"[WARNING] Cannot find UltraPermissions! This plugin would not work without UntraPermissions plugin.");
            availability = false;
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage(prefix + ChatColor.RED+"이 명령어는 플레이어에 의해서만 실행될 수 있습니다.");
            return true;
        }
        if(!label.equalsIgnoreCase("직업부스트") && !label.equals("jb") && !label.equals("직부")){
            return true;
        }

        String name = "";
        if(args.length == 1) {
            if(sender.isOp() && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore())
                name = args[0];
            else
                name = ((Player) sender).getName();
        }else {
            name = ((Player) sender).getName();
        }
        UltraPermissions uperm = new UltraPermissions((JavaPlugin) Bukkit.getPluginManager().getPlugin("UltraPermissions"));
        User user = uperm.getUsers().name(name).orElse(null);
        if(user == null){
            return true;
        }
        List<Permission> userPerms = user.getPermissions();
        boolean nowIn = false;
        for(Permission each : userPerms){
            if(each.getName().contains("jobs.boost.all.all.")){
                String date;
                if(each.getExpiration() == 0) //check if this permission is permanent
                    date = ChatColor.translateAlternateColorCodes('&', "&2&l무제한");
                else
                    date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(each.getExpiration()));

                sender.sendMessage(prefix+ net.md_5.bungee.api.ChatColor.GOLD + " 현재 이용 중인 직업 부스트의 만기: " +net.md_5.bungee.api.ChatColor.of(new Color(0,255,182)) + date);
                long gap = (each.getExpiration() - System.currentTimeMillis()) / 1000;
                long days = gap / 86400;
                long hours = (gap - (days * 86400)) / 3600;
                long minutes = (gap - (days * 86400) - (hours * 3600)) / 60;
                long seconds = gap - (days * 86400) - (hours * 3600) - (minutes * 60);
                sender.sendMessage(prefix+ net.md_5.bungee.api.ChatColor.GOLD + " 남은 기간은 " + ChatColor.YELLOW + days + ChatColor.GOLD + "일 " + ChatColor.YELLOW + hours + ChatColor.GOLD + "시간 " + ChatColor.YELLOW + minutes + ChatColor.GOLD + "분 " + ChatColor.YELLOW + seconds + ChatColor.GOLD + "초 입니다.");
                nowIn = true;
            }
        }
        if(!nowIn){
            sender.sendMessage(prefix+ net.md_5.bungee.api.ChatColor.RED +" 현재 이용 중인 직업 부스트가 없습니다.");
        }
        return true;
    }

}