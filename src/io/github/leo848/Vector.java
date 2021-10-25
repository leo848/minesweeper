package io.github.leo848;

import java.awt.*;
import java.io.*;

@SuppressWarnings({"unused", "StandardVariableNames", "UnusedReturnValue"})
public class Vector implements Serializable {
	public float x;
	
	public float y;
	
	public float z;
	
	transient protected float[] array;
	
	public Vector() {
	}
	
	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	public Vector(Point location) {
		this(location.x, location.y);
	}
	
	static public Vector add(Vector v1, Vector v2) {
		return add(v1, v2, null);
	}
	
	static public Vector add(Vector v1, Vector v2, Vector target) {
		if (target == null) {
			target = new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		} else {
			target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		}
		return target;
	}
	
	static public float angleBetween(Vector v1, Vector v2) {
		
		
		if (v1.x == 0 && v1.y == 0 && v1.z == 0) return 0.0f;
		if (v2.x == 0 && v2.y == 0 && v2.z == 0) return 0.0f;
		
		double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
		double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
		double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
		
		double amt = dot / (v1mag * v2mag);
		
		
		if (amt <= -1) {
			return (float) Math.PI;
		} else if (amt >= 1) {
			
			return 0;
		}
		return (float) Math.acos(amt);
	}
	
	static public Vector cross(Vector v1, Vector v2, Vector target) {
		float crossX = v1.y * v2.z - v2.y * v1.z;
		float crossY = v1.z * v2.x - v2.z * v1.x;
		float crossZ = v1.x * v2.y - v2.x * v1.y;
		
		if (target == null) {
			target = new Vector(crossX, crossY, crossZ);
		} else {
			target.set(crossX, crossY, crossZ);
		}
		return target;
	}
	
	static public float dist(Vector v1, Vector v2) {
		float dx = v1.x - v2.x;
		float dy = v1.y - v2.y;
		float dz = v1.z - v2.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	static public Vector div(Vector v, float n) {
		return div(v, n, null);
	}
	
	static public Vector div(Vector v, float n, Vector target) {
		if (target == null) {
			target = new Vector(v.x / n, v.y / n, v.z / n);
		} else {
			target.set(v.x / n, v.y / n, v.z / n);
		}
		return target;
	}
	
	static public float dot(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}
	
	static public Vector fromAngle(float angle) {
		return fromAngle(angle, null);
	}
	
	static public Vector fromAngle(float angle, Vector target) {
		if (target == null) {
			target = new Vector((float) Math.cos(angle), (float) Math.sin(angle), 0);
		} else {
			target.set((float) Math.cos(angle), (float) Math.sin(angle), 0);
		}
		return target;
	}
	
	public boolean isEmpty() {
		return x == 0 && y == 0 && z == 0;
	}
	
	static public Vector mult(Vector v, float n) {
		return mult(v, n, null);
	}
	
	static public Vector mult(Vector v, float n, Vector target) {
		if (target == null) {
			target = new Vector(v.x * n, v.y * n, v.z * n);
		} else {
			target.set(v.x * n, v.y * n, v.z * n);
		}
		return target;
	}
	
	public Vector mod(float n) {
		this.x %= n;
		this.y %= n;
		this.z %= n;
		
		return this;
	}
	
	public Vector set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	static public Vector sub(Vector v1, Vector v2) {
		return sub(v1, v2, null);
	}
	
	static public Vector sub(Vector v1, Vector v2, Vector target) {
		if (target == null) {
			target = new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		} else {
			target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		}
		return target;
	}
	
	public Vector add(Vector v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}
	
	public Vector add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public float[] array() {
		if (array == null) {
			array = new float[3];
		}
		array[0] = x;
		array[1] = y;
		array[2] = z;
		return array;
	}
	
	public Vector cross(Vector v) {
		return cross(v, null);
	}
	
	public Vector cross(Vector v, Vector target) {
		float crossX = y * v.z - v.y * z;
		float crossY = z * v.x - v.z * x;
		float crossZ = x * v.y - v.x * y;
		
		if (target == null) {
			target = new Vector(crossX, crossY, crossZ);
		} else {
			target.set(crossX, crossY, crossZ);
		}
		return target;
	}
	
	public float dist(Vector v) {
		float dx = x - v.x;
		float dy = y - v.y;
		float dz = z - v.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public float dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public float dot(float x, float y, float z) {
		return this.x * x + this.y * y + this.z * z;
	}
	
	@Deprecated
	public Vector get() {
		return copy();
	}
	
	public void ensureNotNaN() {
		if (Float.isNaN(x)) x = 0f;
		if (Float.isNaN(y)) y = 0f;
		if (Float.isNaN(z)) z = 0f;
	}
	
	public Vector copy() {
		return new Vector(x, y, z);
	}
	
	public float[] get(float[] target) {
		if (target == null) {
			return new float[] {x, y, z};
		}
		if (target.length >= 2) {
			target[0] = x;
			target[1] = y;
		}
		if (target.length >= 3) {
			target[2] = z;
		}
		return target;
	}
	
	@Deprecated
	public float heading2D() {
		return heading();
	}
	
	public float heading() {
		return (float) Math.atan2(y, x);
	}
	
	public Vector limit(float max) {
		if (magSq() > max * max) {
			normalize();
			mult(max);
		}
		return this;
	}
	
	public float magSq() {
		return (x * x + y * y + z * z);
	}
	
	public Vector mult(float n) {
		x *= n;
		y *= n;
		z *= n;
		return this;
	}
	
	public Vector normalize() {
		float v = mag();
		if (v != 0 && v != 1) {
			div(v);
		}
		return this;
	}
	
	public float mag() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vector div(float n) {
		x /= n;
		y /= n;
		z /= n;
		return this;
	}
	
	public Vector div(Vector v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}
	
	public Vector set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector set(Vector v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}
	
	public Vector set(float[] source) {
		if (source.length >= 2) {
			x = source[0];
			y = source[1];
		}
		if (source.length >= 3) {
			z = source[2];
		}
		return this;
	}
	
	public Vector setMag(float len) {
		normalize();
		mult(len);
		return this;
	}
	
	public Vector setMag(Vector target, float len) {
		target = normalize(target);
		target.mult(len);
		return target;
	}
	
	public Vector normalize(Vector target) {
		if (target == null) {
			target = new Vector();
		}
		float m = mag();
		if (m > 0) {
			target.set(x / m, y / m, z / m);
		} else {
			target.set(x, y, z);
		}
		return target;
	}
	
	public Vector sub(Vector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}
	
	public Vector sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + Float.floatToIntBits(x);
		result = 31 * result + Float.floatToIntBits(y);
		result = 31 * result + Float.floatToIntBits(z);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof final Vector p)) {
			return false;
		}
		return x == p.x && y == p.y && z == p.z;
	}
	
	@Override
	public String toString() {
		return "[ " + x + ", " + y + ", " + z + " ]";
	}
}