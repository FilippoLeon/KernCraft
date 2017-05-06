package com.R3DKn16h7.kerncraft.items.energy;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.R3DKn16h7.kerncraft.capabilities.element.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.element.ElementContainerAndEnergyProvider;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.items.EnergyContainerItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class GenericBattery extends EnergyContainerItem
        implements IBauble {
    static public List<Tuple<Integer, Integer>> ELEMENT_PAIRS = new ArrayList<>();

    public GenericBattery() {

        super();

        addPair("Li", "Co");
        addPair("Zn", "C");
        addPair("Na", "F");
        addPair("Na", "S");
        addPair("K", "Co");
        addPair("Ni", "Fe");
        addPair("Ni", "Cd");
        addPair("Pb", "S");
        addPair("Al", "Co");
    }

    static public void addPair(String elem1, String elem2) {

        ELEMENT_PAIRS.add(new Tuple<>(
                        ElementRegistry.symbolToId(elem1),
                        ElementRegistry.symbolToId(elem2)
                )
        );
    }

    @Override
    public String getName() {
        return "generic_battery";
    }

    @Override
    protected int getMaxInput() {
        return 100;
    }

    @Override
    protected int getMaxOutput() {
        return 100;
    }

    @Override
    protected int getCapacity() {
        return 1000;
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        NBTTagCompound nbt = super.getNBTShareTag(stack);

        IElementContainer cap = ElementCapabilities.getCapability(stack);

        NBTTagCompound nbt2 = cap.serializeNBT();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        nbt.setTag("capabilityElementContainer", nbt2);

        return nbt;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote && ElementCapabilities.hasCapability(stack)) {
            IElementContainer cap = ElementCapabilities.getCapability(stack);
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("capabilityElementContainer")) {
                cap.deserializeNBT(stack.getTagCompound().getCompoundTag("capabilityElementContainer"));
            }
        }
        if (!worldIn.isRemote) {

            IEnergyStorage energyCap = stack.getCapability(CapabilityEnergy.ENERGY,
                    null);
            IElementContainer elementCap = stack.getCapability(ElementCapabilities.CAPABILITY_ELEMENT_CONTAINER,
                    null);

            for (Tuple<Integer, Integer> pair : ELEMENT_PAIRS) {
                int LiAmount = elementCap.getAmountOf(pair.getFirst());
                int CoAmount = elementCap.getAmountOf(pair.getSecond());
                if (LiAmount >= 4 && CoAmount >= 0) {
                    int receivable = energyCap.receiveEnergy(
                            100, true
                    );
                    if (receivable > 0) {
                        energyCap.receiveEnergy(
                                100, false
                        );
                        elementCap.removeAmountOf(pair.getFirst(), 4, false);
                        elementCap.removeAmountOf(pair.getSecond(), 4, false);
                        break;
                    }
                }
            }

            int maxExtract = energyCap.extractEnergy(100, true);
            int leftover = maxExtract;
            if (entityIn instanceof EntityPlayer) {
                InventoryPlayer inv = ((EntityPlayer) entityIn).inventory;
                for (int i = 0; i < inv.getSizeInventory(); ++i) {
                    ItemStack slotStack = inv.getStackInSlot(i);
                    if (slotStack == stack) continue;
                    if (leftover == 0) {
                        break;
                    }
                    if (slotStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        IEnergyStorage cap = slotStack.getCapability(CapabilityEnergy.ENERGY, null);
                        leftover -= cap.receiveEnergy(leftover, false);
                    }
                }
            }
            energyCap.extractEnergy(maxExtract - leftover, false);
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn,
                               List<String> tooltip, boolean advanced) {
        ElementCapabilities.addTooltip(stack, tooltip);

        super.addInformation(stack, playerIn, tooltip, advanced);
    }


    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        ElementContainerAndEnergyProvider cap
                = new ElementContainerAndEnergyProvider(
                2, 1000,
                1000, 100, 100
        );
        return cap;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.CHARM;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (player.world.isRemote) return;


    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {

        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return false;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean onLeftClickEntity(ItemStack stack,
                                     EntityPlayer player,
                                     Entity entity) {
        return true;
    }
}