package com.R3DKn16h7.quantumbase.tileentities;

import com.R3DKn16h7.quantumbase.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.ArrayList;

public class ExtractorTileEntity extends TileEntity
        implements ITickable, IInventory {

    // Slot ISs
    static public int inputSlot = 0;
    static public int catalystSlot = 1;
    static public int canisterSlot = 2;
    static public int fuelSlot = 3;
    static public int outputSlotStart = 4;
    static public int outputSlotSize = 4;
    static private int NumberOfSlots = 8;
    // Static register of recipes
    static private ArrayList<ExtractorRecipe> recipes;
    private ItemStack[] inventory;
    private String customName;
    private int progress;
    private int progressMax = 10;
    private float elapsed = 0f;
    private float ticTime = 5f;
    // Are we currently smelting
    private boolean smelting = false;
    private int smeltingId = -1;
    private boolean inputChanged = false;

    ExtractorTileEntity() {
        this.inventory = new ItemStack[this.getSizeInventory()];

        if (recipes == null) {
            recipes = new ArrayList<ExtractorRecipe>();
            registerRecipe(Item.getItemFromBlock(Blocks.IRON_ORE),
                    Item.getItemFromBlock(Blocks.ANVIL),
                    new ElementStack[]{new ElementStack(1, 1), new ElementStack(4, 1)}, 1000);
            registerRecipe(Item.getItemFromBlock(Blocks.DIAMOND_ORE),
                    Item.getItemFromBlock(Blocks.LAPIS_BLOCK),
                    new ElementStack[]{new ElementStack(1, 1), new ElementStack(5, 5)}, 1000);
        }
    }

    static public boolean registerRecipe(Item item, Item catalyst,
                                         ElementStack[] outs, int energy) {
        recipes.add(new ExtractorRecipe(item, catalyst, outs, energy));
        return true;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public float getProgressPerc() {
        return progress / (float) progressMax;
    }

    public boolean canSmelt() {
        return canSmelt(smeltingId);
    }

    public boolean canSmelt(int idx) {
        if (idx == -1) return false;
        System.out.print("Hello" + idx);
        ExtractorRecipe recipe = recipes.get(idx);
        if (inventory == null ||
                // Check input
                inventory[inputSlot] == null ||
                inventory[inputSlot].getItem() != recipe.item ||
                inventory[inputSlot].stackSize < 1 ||
                // Check catalyst
                inventory[catalystSlot] == null ||
                inventory[catalystSlot].getItem() != recipe.catalyst ||
                inventory[catalystSlot].stackSize < 1
            // Check canister
//           inventory[canisterSlot] == null ||
//           inventory[canisterSlot].getItem() != ModItems.canister
                ) {

//            Minecraft.getMinecraft().thePlayer.sendChatMessage("Cant smelt no recipe" + idx);
            return false;
        }

//        boolean free = false;
//        for(int i = outputSlotStart; i < outputSlotStart + outputSlotStart; ++i) {
//            if(inventory[i] == null) free = true;
//        }
//        // Check space in output slot
//        if(!free) {
//            Minecraft.getMinecraft().thePlayer.sendChatMessage("Cant smelt no free" + idx);
//            return false;
//            
//        }

//        Minecraft.getMinecraft().thePlayer.sendChatMessage("Can smelt" + idx);
        return true;
    }

    public void progressSmelting() {
        if (inputChanged) {
            findRecipe();
            inputChanged = false;
        }
        if (canSmelt()) {
            progress++;
            if (progress >= progressMax) {
                progress = 0;
                doneSmelting();
            }
        } else {
            progress = 0;
        }
    }

    public void doneSmelting() {
        inventory[inputSlot] = new ItemStack(inventory[inputSlot].getItem(),
                inventory[inputSlot].stackSize - 1);
        if (recipes.get(smeltingId).catalyst != null) {
            inventory[catalystSlot] = new ItemStack(inventory[catalystSlot].getItem(),
                    inventory[catalystSlot].stackSize - 1);
        }

        boolean[] done_out = new boolean[recipes.get(smeltingId).outs.length];

        for (int rec_slot = outputSlotStart; rec_slot < outputSlotStart + outputSlotSize; ++rec_slot) {

            if (inventory[rec_slot] == null) {
                continue;
            }

            NBTTagCompound nbt;
            if (inventory[rec_slot].hasTagCompound()) {
                nbt = inventory[rec_slot].getTagCompound();
            } else {
                nbt = new NBTTagCompound();
            }

            Minecraft.getMinecraft().thePlayer.sendChatMessage("enasdasdti " + recipes.get(smeltingId).outs.length);
            int i = 0;
            for (ElementStack rec_out : recipes.get(smeltingId).outs) {
                if (done_out[i]) {
                    ++i;
                    continue;
                }
                Minecraft.getMinecraft().thePlayer.sendChatMessage("enti" + i);
                if (nbt.hasKey("Element")) {
                    int elemId = nbt.getInteger("Element");
                    int qty = nbt.getInteger("Quantity");
                    if (elemId == rec_out.id) {
                        nbt.setInteger("Quantity", rec_out.quantity + qty);
                        inventory[rec_slot].setTagCompound(nbt);
                        done_out[i] = true;
                        break;
                    }
                } else {
                    nbt.setInteger("Element", rec_out.id);
                    nbt.setInteger("Quantity", rec_out.quantity);
                    inventory[rec_slot].setTagCompound(nbt);
                    done_out[i] = true;
                    break;
                }
                ++i;
            }
        }

        for (int rec_slot = outputSlotStart; rec_slot < outputSlotStart + outputSlotSize; ++rec_slot) {

            if (inventory[rec_slot] == null) {
                int i = 0;
                for (ElementStack rec_out : recipes.get(smeltingId).outs) {

                    if (done_out[i] == false && inventory[canisterSlot] != null &&
                            inventory[canisterSlot].stackSize > 0 &&
                            inventory[canisterSlot].getItem() == ModItems.canister) {
                        done_out[i] = true;
                        inventory[canisterSlot] = new ItemStack(inventory[canisterSlot].getItem(),
                                inventory[canisterSlot].stackSize - 1);
                        inventory[rec_slot] = new ItemStack(inventory[canisterSlot].getItem(), 1);
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setInteger("Element", rec_out.id);
                        nbt.setInteger("Quantity", rec_out.quantity);
                        inventory[rec_slot].setTagCompound(nbt);
                    }

                    ++i;
                }
            }
        }
    }

    public void abortSmelting() {
        smelting = false;
        smeltingId = -1;
    }

    public void findRecipe() {
        int idx = 0;
        for (ExtractorRecipe recipe : recipes) {
            if (canSmelt(idx)) {
                smeltingId = idx;
                smelting = true;
                return;
            }

            ++idx;
        }
        smeltingId = -1;
        smelting = false;

    }


    @Override
    public void update() {
        if (elapsed > ticTime) {
            progressSmelting();
            elapsed = 0;
        } else {
            elapsed += 1;
        }
//    	Minecraft.getMinecraft().thePlayer.sendChatMessage("Lol what a noob2");
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? "Name" : "container.tutorial_tile_entity";
    }

    @Override
    public boolean hasCustomName() {
        return "Name" != null && !"Name".equals("");
    }

    @Override
    public int getSizeInventory() {
        return NumberOfSlots;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return null;
        return this.inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != null) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).stackSize <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, null);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).stackSize <= 0) {
                    this.setInventorySlotContents(index, null);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {


        if (index < 0 || index >= this.getSizeInventory())
            return;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();
        if (stack != null && (index == inputSlot || index == catalystSlot)) {
            inputChanged = true;
        }

        if (stack != null && stack.stackSize == 0)
            stack = null;

        this.inventory[index] = stack;
        this.markDirty();
    }


    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.getPos()) == this &&
                player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == canisterSlot || index >= outputSlotStart || index < outputSlotStart + outputSlotSize) {
            if (stack.getItem() != ModItems.canister) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSizeInventory(); i++)
            this.setInventorySlotContents(i, null);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        nbt.setTag("Items", list);

        if (this.hasCustomName()) {
            nbt.setString("CustomName", "Name");
        }

        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        NBTTagList list = nbt.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
        }

        if (nbt.hasKey("CustomName", 8)) {
        }
    }

    static public class ElementStack {
        public int id;
        public int quantity;

        public ElementStack(int id_, int quantity_) {
            id = id_;
            quantity = quantity_;
        }
    }

    static public class ExtractorRecipe {
        public Item item;
        public Item catalyst;
        public ElementStack[] outs;
        public int energy;

        public ExtractorRecipe(Item item_, Item catalyst_,
                               ElementStack[] outs_, int energy_) {
            item = item_;
            catalyst = catalyst_;
            outs = outs_;
            energy = energy_;
        }
    }
}
