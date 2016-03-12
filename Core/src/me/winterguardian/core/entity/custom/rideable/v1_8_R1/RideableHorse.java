package me.winterguardian.core.entity.custom.rideable.v1_8_R1;

import me.winterguardian.core.entity.custom.rideable.RideableEntity;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;

import java.lang.reflect.Field;

public class RideableHorse extends EntityHorse implements RideableEntity
{
	private float climbHeight, jumpHeight, jumpThrust, speed, backwardSpeed, sidewaySpeed;

	public RideableHorse(org.bukkit.World world)
	{
		this(((CraftWorld)world).getHandle());
	}

	public RideableHorse(World world)
	{
		super(world);
		this.climbHeight = 1f;
		this.jumpHeight = 1f;
		this.jumpThrust = 1f;
		this.speed = 1f;
		this.backwardSpeed = 0.25f;
		this.sidewaySpeed = 0.4f;
		
		this.goalSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
	}
	
	@Override
	public void g(float sideMot, float forMot)
	{
		
		if(this.passenger == null || !(this.passenger instanceof EntityHuman))
		{
			this.S = 1f; 
			super.g(sideMot, forMot);
			return;
		}
		
		this.lastYaw = this.yaw = this.passenger.yaw;
		this.pitch = this.passenger.pitch * 0.75f;
		if(this.pitch > 0)
			this.pitch = 0;
		this.setYawPitch(this.yaw, this.pitch);
		this.aK = this.aI = this.yaw;
	
		this.S = this.climbHeight; 
	
		boolean jump = false;
		
		try
		{
			Field field = EntityLiving.class.getDeclaredField("aY");
			field.setAccessible(true);
			jump = (boolean) field.get(this.passenger);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		sideMot = ((EntityLiving) this.passenger).aX;
		forMot = ((EntityLiving) this.passenger).aY;

		if (forMot < 0.0F)
			forMot *= this.backwardSpeed;
	
		sideMot *= this.sidewaySpeed;
	 
		if(jump)
			if(this.inWater)
				this.bG();
			else if(this.onGround && this.jumpHeight != 0 && this.jumpThrust != 0)
			{
				this.motY = this.jumpHeight / 2;
				this.motZ = Math.cos(Math.toRadians(-this.yaw)) * this.jumpThrust * forMot; //normal X
				this.motX = Math.sin(Math.toRadians(-this.yaw)) * this.jumpThrust * forMot; //normal Y
			}

		this.j(this.speed / 5);
		superg(sideMot, forMot);
	}
	
	public void superg(float f, float f1)
	{
		if(!this.world.isStatic)
	    {
	      if ((V()))
	      {
	        double d0 = this.locY;
	        float f3 = 0.8F;
	        float f4 = 0.02F;
	        float f2 = EnchantmentManager.b(this);
	        if (f2 > 3.0F) {
	          f2 = 3.0F;
	        }
	        if (!this.onGround) {
	          f2 *= 0.5F;
	        }
	        if (f2 > 0.0F)
	        {
	          f3 += (0.54600006F - f3) * f2 / 3.0F;
	          f4 += (bH() * 1.0F - f4) * f2 / 3.0F;
	        }
	        a(f, f1, f4);
	        move(this.motX, this.motY, this.motZ);
	        this.motX *= f3;
	        this.motY *= 0.800000011920929D;
	        this.motZ *= f3;
	        this.motY -= 0.02D;
	        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
	          this.motY = 0.30000001192092896D;
	        }
	      }
	      else if ((ab()))
	      {
	        double d0 = this.locY;
	        a(f, f1, 0.02F);
	        move(this.motX, this.motY, this.motZ);
	        this.motX *= 0.5D;
	        this.motY *= 0.5D;
	        this.motZ *= 0.5D;
	        this.motY -= 0.02D;
	        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
	          this.motY = 0.30000001192092896D;
	        }
	      }
	      else
	      {
	        float f5 = 0.91F;
	        if (this.onGround) {
	          f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
	        }
	        float f6 = 0.16277136F / (f5 * f5 * f5);
	        float f3;
	        if (this.onGround) {
	          f3 = bH() * f6;
	        } else {
	          f3 = this.aK;
	        }
	        a(f, f1, f3);
	        f5 = 0.91F;
	        if (this.onGround) {
	          f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
	        }
	        if (j_())
	        {
	          float f4 = 0.15F;
	          this.motX = MathHelper.a(this.motX, -f4, f4);
	          this.motZ = MathHelper.a(this.motZ, -f4, f4);
	          this.fallDistance = 0.0F;
	          if (this.motY < -0.15D) {
	            this.motY = -0.15D;
	          }
	        }
	        move(this.motX, this.motY, this.motZ);
	        if ((this.positionChanged) && (j_())) {
	          this.motY = 0.2D;
	        }
	        if ((this.world.isStatic) && ((!this.world.isLoaded(new BlockPosition((int)this.locX, 0, (int)this.locZ))) || (!this.world.getChunkAtWorldCoords(new BlockPosition((int)this.locX, 0, (int)this.locZ)).o())))
	        {
	          if (this.locY > 0.0D) {
	            this.motY = -0.1D;
	          } else {
	            this.motY = 0.0D;
	          }
	        }
	        else {
	          this.motY -= 0.08D;
	        }
	        this.motY *= 0.9800000190734863D;
	        this.motX *= f5;
	        this.motZ *= f5;
	      }
	    }
	    this.ay = this.ax;
	    double d0 = this.locX - this.lastX;
	    double d1 = this.locZ - this.lastZ;
	    
	    float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
	    if (f2 > 1.0F) {
	      f2 = 1.0F;
	    }
	    this.az += (f2 - this.az) * 0.4F;
	    this.aA += this.az;
	}
	
	@Override
	public float getClimbHeight()
	{
		return this.climbHeight;
	}

	@Override
	public void setClimbHeight(float climbHeight)
	{
		this.climbHeight = climbHeight;
	}

	@Override
	public float getJumpHeight()
	{
		return this.jumpHeight;
	}

	@Override
	public void setJumpHeight(float jumpHeight)
	{
		this.jumpHeight = jumpHeight;
	}
	
	@Override
	public float getJumpThrust()
	{
		return this.jumpThrust;
	}

	@Override
	public void setJumpThrust(float jumpThrust)
	{
		this.jumpThrust = jumpThrust;
	}

	@Override
	public float getSpeed()
	{
		return this.speed;
	}

	@Override
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	@Override
	public float getBackwardSpeed()
	{
		return this.backwardSpeed;
	}

	@Override
	public void setBackwardSpeed(float backwardSpeed)
	{
		this.backwardSpeed = backwardSpeed;
	}

	@Override
	public float getSidewaySpeed()
	{
		return this.sidewaySpeed;
	}

	@Override
	public void setSidewaySpeed(float sidewaySpeed)
	{
		this.sidewaySpeed = sidewaySpeed;
	}

}