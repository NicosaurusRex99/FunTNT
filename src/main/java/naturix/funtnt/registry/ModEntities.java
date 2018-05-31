package naturix.funtnt.registry;

import naturix.funtnt.FunTNT;
import naturix.funtnt.blocks.TNTEntities.NewTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {
    public static void init() {
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(FunTNT.MODID, "newtnt"), NewTNT.class, "NewTNT", id++, FunTNT.instance, 64, 3, true); 
       
    }	
    
    

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        //put mobs registry here,, replace entity, render, and model with the model, render, and entity classes of the desired entity.
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        // RenderingRegistry.registerEntityRenderingHandler(Entity.class, new Render(renderManager, new Model(), 0.5F)); // 0.5F is shadow size 
    }
}